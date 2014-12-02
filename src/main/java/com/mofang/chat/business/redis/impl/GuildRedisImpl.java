package com.mofang.chat.business.redis.impl;

import org.json.JSONObject;

import com.mofang.chat.business.redis.GuildRedis;
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
public class GuildRedisImpl implements GuildRedis
{
	private final static GuildRedisImpl REDIS = new GuildRedisImpl();
	
	private GuildRedisImpl()
	{}
	
	public static GuildRedisImpl getInstance()
	{
		return REDIS;
	}

	@Override
	public JSONObject getInfo(long guildId) throws Exception
	{
		String key = SysRedisKey.GUILD_INFO_KEY_PREFIX + guildId;
		RedisWorker<String> worker = new GetWorker(key);
		String value = SysObject.GUILD_SLAVE_EXECUTOR.execute(worker);
		if(StringUtil.isNullOrEmpty(value))
			return null;
		
		JSONObject json = new JSONObject(value);
		return json;
	}
}