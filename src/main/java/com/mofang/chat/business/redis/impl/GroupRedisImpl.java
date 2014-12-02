package com.mofang.chat.business.redis.impl;

import java.util.Map;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.redis.GroupRedis;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.chat.business.sysconf.SysRedisKey;
import com.mofang.framework.data.redis.RedisWorker;
import com.mofang.framework.data.redis.workers.GetWorker;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class GroupRedisImpl implements GroupRedis
{
	private final static GroupRedisImpl REDIS = new GroupRedisImpl();
	
	private GroupRedisImpl()
	{}
	
	public static GroupRedisImpl getInstance()
	{
		return REDIS;
	}

	@Override
	public JSONObject getInfo(long groupId) throws Exception
	{
		String key = SysRedisKey.GUILD_GROUP_INFO_KEY_PREFIX + groupId;
		RedisWorker<String> worker = new GetWorker(key);
		String value = SysObject.GUILD_SLAVE_EXECUTOR.execute(worker);
		if(StringUtil.isNullOrEmpty(value))
			return null;
		
		JSONObject json = new JSONObject(value);
		return json;
	}
	
	@Override
	public Map<String, String> getUserList(final long groupId) throws Exception
	{
		RedisWorker<Map<String, String>> worker = new RedisWorker<Map<String, String>>()
		{
			@Override
			public Map<String, String> execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.GUILD_GROUP_USER_LIST_KEY_PREFIX + groupId;
				return jedis.hgetAll(key);
			}
		};
		return SysObject.GUILD_SLAVE_EXECUTOR.execute(worker);
	}
}