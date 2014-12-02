package com.mofang.chat.business.redis;

import java.util.Map;

import org.json.JSONObject;

/**
 * 
 * @author zhaodx
 *
 */
public interface GroupRedis
{
	public JSONObject getInfo(long groupId) throws Exception;
	
	public Map<String, String> getUserList(long groupId) throws Exception; 
}