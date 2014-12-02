package com.mofang.chat.business.redis.impl;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.redis.WriteQueueRedis;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.chat.business.sysconf.SysRedisKey;
import com.mofang.framework.data.redis.RedisWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class WriteQueueRedisImpl implements WriteQueueRedis
{
	private final static WriteQueueRedisImpl REDIS = new WriteQueueRedisImpl();
	
	private WriteQueueRedisImpl()
	{}
	
	public static WriteQueueRedisImpl getInstance()
	{
		return REDIS;
	}

	@Override
	public boolean put(final String task) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				jedis.lpush(SysRedisKey.WRITE_QUEUE_CHAT_MESSAGE_KEY, task);
				return true;
			}
		};
		return SysObject.WRITE_QUEUE_EXECUTOR.execute(worker);
	}
	
	@Override
	public String get() throws Exception
	{
		RedisWorker<String> worker = new RedisWorker<String>()
		{
			@Override
			public String execute(Jedis jedis) throws Exception 
			{
				return jedis.rpop(SysRedisKey.WRITE_QUEUE_CHAT_MESSAGE_KEY);
			}
		};
		return SysObject.WRITE_QUEUE_EXECUTOR.execute(worker);
	}

	@Override
	public boolean putAppleNotify(final String notify) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				jedis.lpush(SysRedisKey.APPLE_MESSAGE_PUSH_QUEUE_KEY, notify);
				return true;
			}
		};
		return SysObject.WRITE_QUEUE_EXECUTOR.execute(worker);
	}
}