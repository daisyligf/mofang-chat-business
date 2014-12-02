package com.mofang.chat.business.redis;

import java.util.Set;

/**
 * 
 * @author zhaodx
 *
 */
public interface RoomRedis
{
	public boolean enterRoom(int roomId, long userId) throws Exception;
	
	public boolean quitRoom(int roomId, long userId) throws Exception;
	
	public Set<String> getEnterUsers(int roomId) throws Exception;
	
	public int getEnterUserCount(int roomId) throws Exception;
	
	public long getEnterTimestamp(int roomId, long userId) throws Exception;
	
	public boolean setLastTimestamp(int roomId, long timestamp) throws Exception;
	
	public long getLastTimestamp(int roomId) throws Exception;
	
	public boolean subscribeRoom(int roomId, long userId, double score) throws Exception;
	
	public boolean unsubscribeRoom(int roomId, long userId) throws Exception;
	
	public boolean clearSubscribeRoom(int roomId) throws Exception;
	
	public Set<String> getSubscribeUsers(int roomId, int start, int size) throws Exception;
	
	public long getSubscribeUserCount(int roomId) throws Exception;
}