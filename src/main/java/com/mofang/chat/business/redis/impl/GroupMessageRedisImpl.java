package com.mofang.chat.business.redis.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.entity.GroupMessage;
import com.mofang.chat.business.model.ChatGroupMessage;
import com.mofang.chat.business.redis.GroupMessageRedis;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.chat.business.sysconf.SysRedisKey;
import com.mofang.framework.data.redis.RedisWorker;
import com.mofang.framework.data.redis.workers.IncrWorker;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class GroupMessageRedisImpl implements GroupMessageRedis
{
	private final static GroupMessageRedisImpl REDIS = new GroupMessageRedisImpl();
	
	private GroupMessageRedisImpl()
	{}
	
	public static GroupMessageRedisImpl getInstance()
	{
		return REDIS;
	}

	@Override
	public long getMaxMessageId() throws Exception
	{
		RedisWorker<Long> worker = new IncrWorker(SysRedisKey.GROUP_MESSAGE_ID_INCREMENT_KEY);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean save(final ChatGroupMessage model, final int maxCount) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String messageInfoKey = SysRedisKey.GROUP_MESSAGE_INFO_KEY_PREFIX + model.getMessageId();
				String messageListKey = SysRedisKey.GROUP_MESSAGE_LIST_KEY_PREFIX + model.getGroupId();
				
				Long count = jedis.zcard(messageListKey);
				///超过群组最大消息条数
				if(count >= maxCount)
				{
					Set<String> messages = jedis.zrange(messageListKey, 0, (count - maxCount));
					Iterator<String> iterator = messages.iterator();
					while(iterator.hasNext())
					{
						String messageId = (String)iterator.next();
						///将messageId从groupId对应的message列表中删除
						jedis.zrem(messageListKey, messageId);
						///将message信息从redis中删除
						jedis.del(SysRedisKey.GROUP_MESSAGE_INFO_KEY_PREFIX + messageId);
					}
				}
				
				///将最新的message信息添加到redis中
				JSONObject json = model.toJson();
				jedis.set(messageInfoKey, json.toString());
				
				///将messageId添加到groupId对应的message列表中
				jedis.zadd(messageListKey, model.getMessageId().doubleValue(), model.getMessageId().toString());
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public ChatGroupMessage getInfo(final long messageId)
	{
		RedisWorker<ChatGroupMessage> worker = new RedisWorker<ChatGroupMessage>()
		{
			@Override
			public ChatGroupMessage execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.GROUP_MESSAGE_INFO_KEY_PREFIX + messageId;
				String value = jedis.get(key);
				if(StringUtil.isNullOrEmpty(value))
					return null;
				
				JSONObject json = new JSONObject(value);
				return ChatGroupMessage.toMysqlModel(json);
			}
		};
		try
		{
			return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Override
	public Set<String> getList(final long groupId, final long minMessageId, final long maxMessageId, final int pageSize) throws Exception
	{
		RedisWorker<Set<String>> worker = new RedisWorker<Set<String>>()
		{
			@Override
			public Set<String> execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.GROUP_MESSAGE_LIST_KEY_PREFIX + groupId;
				return jedis.zrevrangeByScore(key, "(" + maxMessageId, "(" + minMessageId, 0, pageSize);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public boolean updateStatus(final long messageId, final int status) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.GROUP_MESSAGE_INFO_KEY_PREFIX + messageId;
				String value = jedis.get(key);
				if(StringUtil.isNullOrEmpty(value))
					return null;
				
				JSONObject json = new JSONObject(value);
				json.put("status", status);
				jedis.set(key, json.toString());
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean incrNotifyUnreadCount(final long userId, final GroupMessage model) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_GROUP_UNREAD_KEY_PREFIX + userId;
				String message = jedis.hget(key, String.valueOf(model.getGroupId()));
				model.setUnreadCount(1);
				if(!StringUtil.isNullOrEmpty(message))
				{
					GroupMessage oldModel = GroupMessage.buildByJson(new JSONObject(message));
					if(null != oldModel)
						model.setUnreadCount(oldModel.getUnreadCount() + 1);
				}
				JSONObject json = model.toJson();
				jedis.hset(key, String.valueOf(model.getGroupId()), json.toString());
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean clearNotifyUnreadCount(final long userId, final long groupId) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_GROUP_UNREAD_KEY_PREFIX + userId;
				jedis.hdel(key, String.valueOf(groupId));
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public GroupMessage getNotifyInfo(final long userId, final long groupId) throws Exception
	{
		RedisWorker<GroupMessage> worker = new RedisWorker<GroupMessage>()
		{
			@Override
			public GroupMessage execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_GROUP_UNREAD_KEY_PREFIX + userId;
				String message = jedis.hget(key, String.valueOf(groupId));
				if(StringUtil.isNullOrEmpty(message))
					return null;
				
				return GroupMessage.buildByJson(new JSONObject(message));
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public Map<Long, GroupMessage> getNotifyList(final long userId) throws Exception
	{
		RedisWorker<Map<Long, GroupMessage>> worker = new RedisWorker<Map<Long, GroupMessage>>()
		{
			@Override
			public Map<Long, GroupMessage> execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_GROUP_UNREAD_KEY_PREFIX + userId;
				Map<String, String> map = jedis.hgetAll(key);
				if(null == map || map.size() == 0)
					return null;
				
				Iterator<String> iterator = map.keySet().iterator();
				Map<Long, GroupMessage> notifyMap = new HashMap<Long, GroupMessage>();
				GroupMessage model = null;
				while(iterator.hasNext())
				{
					String groupId = (String)iterator.next(); 
					String message = map.get(groupId);
					if(StringUtil.isNullOrEmpty(message))
						continue;
						
					model = GroupMessage.buildByJson(new JSONObject(message));
					notifyMap.put(Long.parseLong(groupId), model);
				}
				return notifyMap;
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
}