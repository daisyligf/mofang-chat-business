package com.mofang.chat.business.redis.impl;

import java.util.Iterator;
import java.util.Set;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.model.ChatPrivateMessage;
import com.mofang.chat.business.redis.PrivateMessageRedis;
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
public class PrivateMessageRedisImpl implements PrivateMessageRedis
{
	private final static PrivateMessageRedisImpl REDIS = new PrivateMessageRedisImpl();
	
	private PrivateMessageRedisImpl()
	{}
	
	public static PrivateMessageRedisImpl getInstance()
	{
		return REDIS;
	}
	
	/**
	 * 获取私聊最大ID
	 */
	@Override
	public Long getMaxMessageId() throws Exception
	{
		RedisWorker<Long> worker = new IncrWorker(SysRedisKey.PRIVATE_MESSAGE_ID_INCREMENT_KEY);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}
	
	/**
	 * 保存私聊消息
	 */
	@Override
	public boolean save(final ChatPrivateMessage model, final int maxCount) throws Exception 
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String privateKey = getPrivateKey(model.getUserId(), model.getToUserId());
				String messageInfoKey = SysRedisKey.PRIVATE_MESSAGE_INFO_KEY_PREFIX + model.getMessageId();
				String messageListKey = SysRedisKey.PRIVATE_MESSAGE_LIST_KEY_PREFIX + privateKey;
				
				Long count = jedis.zcard(messageListKey);
				///超过私聊最大消息条数
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
						jedis.del(SysRedisKey.PRIVATE_MESSAGE_INFO_KEY_PREFIX + messageId);
					}
				}
				
				///将最新的message信息添加到redis中
				JSONObject json = model.toJson();
				jedis.set(messageInfoKey, json.toString());
				
				///将messageId添加到私聊对应的message列表中
				jedis.zadd(messageListKey, model.getMessageId().doubleValue(), model.getMessageId().toString());
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}
	
	/**
	 * 删除私聊消息(用于将过期的消息删除)
	 */
	@Override
	public boolean delete(final ChatPrivateMessage model) throws Exception 
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String privateKey = getPrivateKey(model.getUserId(), model.getToUserId());
				String messageInfoKey = SysRedisKey.PRIVATE_MESSAGE_INFO_KEY_PREFIX + model.getMessageId();
				String messageListKey = SysRedisKey.PRIVATE_MESSAGE_LIST_KEY_PREFIX + privateKey;
				
				jedis.del(messageInfoKey);
				jedis.zrem(messageListKey, model.getMessageId().toString());
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	/**
	 * 获取私聊消息信息
	 */
	@Override
	public ChatPrivateMessage getInfo(final long messageId)
	{
		RedisWorker<ChatPrivateMessage> worker = new RedisWorker<ChatPrivateMessage>()
		{
			@Override
			public ChatPrivateMessage execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.PRIVATE_MESSAGE_INFO_KEY_PREFIX + messageId;
				String value = jedis.get(key);
				if(StringUtil.isNullOrEmpty(value))
					return null;
				
				JSONObject json = new JSONObject(value);
				return ChatPrivateMessage.toMysqlModel(json);
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
	 * 获取指定用户的私聊消息列表
	 */
	@Override
	public Set<String> getList(final long fromUserId, final long toUserId, final long minMessageId, final long maxMessageId, final int pageSize) throws Exception
	{
		RedisWorker<Set<String>> worker = new RedisWorker<Set<String>>()
		{
			@Override
			public Set<String> execute(Jedis jedis) throws Exception 
			{
				String privateKey = getPrivateKey(fromUserId, toUserId);
				String key = SysRedisKey.PRIVATE_MESSAGE_LIST_KEY_PREFIX + privateKey;
				return jedis.zrevrangeByScore(key, "(" + maxMessageId, "(" + minMessageId, 0, pageSize);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	private String getPrivateKey(long fromUserId, long toUserId)
	{
		String privateKey = fromUserId + "_" + toUserId;
		if(fromUserId > toUserId)
			privateKey = toUserId + "_" + fromUserId;
		return privateKey;
	}
}