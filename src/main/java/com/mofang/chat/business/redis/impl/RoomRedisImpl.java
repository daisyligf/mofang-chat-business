package com.mofang.chat.business.redis.impl;

import java.util.Set;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.redis.RoomRedis;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.chat.business.sysconf.SysRedisKey;
import com.mofang.framework.data.redis.RedisWorker;
import com.mofang.framework.data.redis.workers.DeleteWorker;
import com.mofang.framework.data.redis.workers.GetWorker;
import com.mofang.framework.data.redis.workers.SetWorker;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class RoomRedisImpl implements RoomRedis
{
	private final static RoomRedisImpl REDIS = new RoomRedisImpl();
	
	private RoomRedisImpl()
	{}
	
	public static RoomRedisImpl getInstance()
	{
		return REDIS;
	}
	
	@Override
	public boolean enterRoom(final int roomId, final long userId) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.ENTER_ROOM_UID_SET_KEY_PREFIX + roomId;
				jedis.hset(key, String.valueOf(userId), String.valueOf(System.currentTimeMillis()));
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean quitRoom(final int roomId, final long userId) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.ENTER_ROOM_UID_SET_KEY_PREFIX + roomId;
				jedis.hdel(key, String.valueOf(userId));
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public Set<String> getEnterUsers(final int roomId) throws Exception
	{
		RedisWorker<Set<String>> worker = new RedisWorker<Set<String>>()
		{
			@Override
			public Set<String> execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.ENTER_ROOM_UID_SET_KEY_PREFIX + roomId;
				return jedis.hkeys(key);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
	
	@Override
	public int getEnterUserCount(final int roomId) throws Exception
	{
		RedisWorker<Integer> worker = new RedisWorker<Integer>()
		{
			@Override
			public Integer execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.ENTER_ROOM_UID_SET_KEY_PREFIX + roomId;
				Long count = jedis.hlen(key);
				if(null == count)
					return 0;
				return count.intValue();
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public long getEnterTimestamp(final int roomId, final long userId) throws Exception
	{
		RedisWorker<Long> worker = new RedisWorker<Long>()
		{
			@Override
			public Long execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.ENTER_ROOM_UID_SET_KEY_PREFIX + roomId;
				String timestamp = jedis.hget(key, String.valueOf(userId));
				if(!StringUtil.isLong(timestamp))
					return 0L;
				return Long.parseLong(timestamp);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public boolean setLastTimestamp(int roomId, long timestamp) throws Exception
	{
		String key = SysRedisKey.ROOM_LAST_TIME_STAMP_KEY_PREFIX + roomId;
		RedisWorker<Boolean> worker = new SetWorker(key, String.valueOf(timestamp));
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public long getLastTimestamp(int roomId) throws Exception
	{
		String key = SysRedisKey.ROOM_LAST_TIME_STAMP_KEY_PREFIX + roomId;
		RedisWorker<String> worker = new GetWorker(key);
		String timestamp = SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
		if(StringUtil.isLong(timestamp))
			return Long.parseLong(timestamp);
		return 0L;
	}

	@Override
	public boolean subscribeRoom(final int roomId, final long userId, final double score) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.SUBSCRIBE_ROOM_UID_SET_KEY_PREFIX + roomId;
				jedis.zadd(key, score, String.valueOf(userId));
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean unsubscribeRoom(final int roomId, final long userId) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.SUBSCRIBE_ROOM_UID_SET_KEY_PREFIX + roomId;
				jedis.zrem(key, String.valueOf(userId));
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean clearSubscribeRoom(int roomId) throws Exception
	{
		String key = SysRedisKey.SUBSCRIBE_ROOM_UID_SET_KEY_PREFIX + roomId;
		RedisWorker<Boolean> worker = new DeleteWorker(key);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public Set<String> getSubscribeUsers(final int roomId, final int start, final int end) throws Exception
	{
		RedisWorker<Set<String>> worker = new RedisWorker<Set<String>>()
		{
			@Override
			public Set<String> execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.SUBSCRIBE_ROOM_UID_SET_KEY_PREFIX + roomId;
				return jedis.zrevrange(key, start, end);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public long getSubscribeUserCount(final int roomId) throws Exception
	{
		RedisWorker<Long> worker = new RedisWorker<Long>()
		{
			@Override
			public Long execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.SUBSCRIBE_ROOM_UID_SET_KEY_PREFIX + roomId;
				return jedis.zcard(key);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
}