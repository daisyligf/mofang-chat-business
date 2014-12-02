package com.mofang.chat.business.redis;

/**
 * 
 * @author zhaodx
 *
 */
public interface PushQueueRedis
{
	public boolean put(String message) throws Exception;
	
	public String get(int dataType) throws Exception;

	public boolean putPushData(String feHost, String message) throws Exception;
	
	public String getPushData(String feHost) throws Exception;
}