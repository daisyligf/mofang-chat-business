package com.mofang.chat.business.service;

import com.mofang.chat.business.entity.User;

/**
 * 
 * @author zhaodx
 *
 */
public interface UserService
{
	public User getInfo(long userId) throws Exception;
	
	public User getAuth(long userId, String sessionId) throws Exception;
	
	public boolean allowSendToUser(long fromUserId, long toUserId) throws Exception;
}