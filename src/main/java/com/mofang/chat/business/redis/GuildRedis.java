package com.mofang.chat.business.redis;

import org.json.JSONObject;

/**
 * 
 * @author zhaodx
 *
 */
public interface GuildRedis
{
	public JSONObject getInfo(long guildId) throws Exception;
}