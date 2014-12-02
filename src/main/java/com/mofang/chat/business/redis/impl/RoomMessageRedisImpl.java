package com.mofang.chat.business.redis.impl;

import java.util.Iterator;
import java.util.Set;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.model.ChatRoomMessage;
import com.mofang.chat.business.redis.RoomMessageRedis;
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
public class RoomMessageRedisImpl implements RoomMessageRedis
{
	private final static RoomMessageRedisImpl REDIS = new RoomMessageRedisImpl();
	
	private RoomMessageRedisImpl()
	{}
	
	public static RoomMessageRedisImpl getInstance()
	{
		return REDIS;
	}
	
	@Override
	public long getMaxMessageId() throws Exception
	{
		RedisWorker<Long> worker = new IncrWorker(SysRedisKey.ROOM_MESSAGE_ID_INCREMENT_KEY);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	/**
	 * 保存公聊消息
	 */
	@Override
	public boolean save(final ChatRoomMessage model, final int maxCount) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String messageInfoKey = SysRedisKey.ROOM_MESSAGE_INFO_KEY_PREFIX + model.getMessageId();
				String messageListKey = SysRedisKey.ROOM_MESSAGE_LIST_KEY_PREFIX + model.getRoomId();
				
				Long count = jedis.zcard(messageListKey);
				///超过房间最大消息条数
				if(count >= maxCount)
				{
					Set<String> messages = jedis.zrange(messageListKey, 0, (count - maxCount));
					Iterator<String> iterator = messages.iterator();
					while(iterator.hasNext())
					{
						String messageId = (String)iterator.next();
						///将messageId从roomId对应的message列表中删除
						jedis.zrem(messageListKey, messageId);
						///将message信息从redis中删除
						jedis.del(SysRedisKey.ROOM_MESSAGE_INFO_KEY_PREFIX + messageId);
					}
				}
				
				///将最新的message信息添加到redis中
				JSONObject json = model.toJson();
				jedis.set(messageInfoKey, json.toString());
				
				///将messageId添加到roomId对应的message列表中
				jedis.zadd(messageListKey, model.getMessageId().doubleValue(), model.getMessageId().toString());
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}
	
	/**
	 * 获取公聊消息信息
	 */
	@Override
	public ChatRoomMessage getInfo(final long messageId)
	{
		RedisWorker<ChatRoomMessage> worker = new RedisWorker<ChatRoomMessage>()
		{
			@Override
			public ChatRoomMessage execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.ROOM_MESSAGE_INFO_KEY_PREFIX + messageId;
				String value = jedis.get(key);
				if(StringUtil.isNullOrEmpty(value))
					return null;
				
				JSONObject json = new JSONObject(value);
				return ChatRoomMessage.toMysqlModel(json);
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
	
	/**
	 * 获取指定房间的公聊消息列表
	 */
	@Override
	public Set<String> getList(final int roomId, final long minMessageId, final long maxMessageId, final int pageSize) throws Exception
	{
		RedisWorker<Set<String>> worker = new RedisWorker<Set<String>>()
		{
			@Override
			public Set<String> execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.ROOM_MESSAGE_LIST_KEY_PREFIX + roomId;
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
				String key = SysRedisKey.ROOM_MESSAGE_INFO_KEY_PREFIX + messageId;
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
}