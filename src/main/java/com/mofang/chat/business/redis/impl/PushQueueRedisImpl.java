package com.mofang.chat.business.redis.impl;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.redis.PushQueueRedis;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.chat.business.sysconf.SysRedisKey;
import com.mofang.chat.business.sysconf.common.PushDataType;
import com.mofang.framework.data.redis.RedisWorker;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class PushQueueRedisImpl implements PushQueueRedis
{
	private final static PushQueueRedisImpl REDIS = new PushQueueRedisImpl();
	
	private PushQueueRedisImpl()
	{}
	
	public static PushQueueRedisImpl getInstance()
	{
		return REDIS;
	}
	
	@Override
	public boolean put(final String message) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				JSONObject json = new JSONObject(message);
				int dataType = json.optInt("push_data_type");
				String key = null;
				if(dataType == PushDataType.ROOM_NOTIFY 
				   || dataType == PushDataType.ROOM_ACTIVITY_NOTIFY
				   || dataType == PushDataType.GROUP_NOTIFY)
					key = SysRedisKey.ROOM_MESSAGE_PUSH_QUEUE_KEY;
				else if(dataType == PushDataType.PRIVATE_NOTIFY 
						|| dataType == PushDataType.FRIEND_NOTIFY
						|| dataType == PushDataType.POST_REPLY_NOTIFY
						|| dataType == PushDataType.SYS_MESSAGE_NOTIFY
						|| dataType == PushDataType.USER_TASK_NOTIFY)
					key = SysRedisKey.PRIVATE_MESSAGE_PUSH_QUEUE_KEY;
				
				if(StringUtil.isNullOrEmpty(key))
					return null;
				jedis.lpush(key, message);
				return true;
			}
		};
		return SysObject.PUSH_QUEUE_EXECUTOR.execute(worker);
	}
	
	@Override
	public String get(final int dataType) throws Exception
	{
		RedisWorker<String> worker = new RedisWorker<String>()
		{
			@Override
			public String execute(Jedis jedis) throws Exception 
			{
				String key = null;
				if(dataType == PushDataType.ROOM_NOTIFY 
				  || dataType == PushDataType.ROOM_ACTIVITY_NOTIFY
				  || dataType == PushDataType.GROUP_NOTIFY)
					key = SysRedisKey.ROOM_MESSAGE_PUSH_QUEUE_KEY;
				else if(dataType == PushDataType.PRIVATE_NOTIFY 
						 || dataType == PushDataType.FRIEND_NOTIFY
						 || dataType == PushDataType.POST_REPLY_NOTIFY
						 || dataType == PushDataType.SYS_MESSAGE_NOTIFY
						 || dataType == PushDataType.USER_TASK_NOTIFY)
					key = SysRedisKey.PRIVATE_MESSAGE_PUSH_QUEUE_KEY;
				
				if(StringUtil.isNullOrEmpty(key))
					return null;
				return jedis.rpop(key);
			}
		};
		return SysObject.PUSH_QUEUE_EXECUTOR.execute(worker);
	}

	@Override
	public boolean putPushData(final String feHost, final String message) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.FRONTEND_PUSH_QUEUE_KEY + feHost;
				jedis.lpush(key, message);
				return true;
			}
		};
		return SysObject.PUSH_QUEUE_EXECUTOR.execute(worker);
	}

	@Override
	public String getPushData(final String feHost) throws Exception
	{
		RedisWorker<String> worker = new RedisWorker<String>()
		{
			@Override
			public String execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.FRONTEND_PUSH_QUEUE_KEY + feHost;
				return jedis.rpop(key);
			}
		};
		return SysObject.PUSH_QUEUE_EXECUTOR.execute(worker);
	}
}