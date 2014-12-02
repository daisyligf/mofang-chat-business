package com.mofang.chat.business.service;

import com.mofang.chat.business.entity.FriendMessage;

/**
 * 
 * @author zhaodx
 *
 */
public interface FriendMessageService
{
	public boolean sendMessage(FriendMessage model) throws Exception;
	
	public FriendMessage getPushNotify(long fromUserId, long toUserId) throws Exception;
}