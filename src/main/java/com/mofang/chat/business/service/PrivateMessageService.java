package com.mofang.chat.business.service;

import java.util.List;

import com.mofang.chat.business.entity.PrivateMessage;
import com.mofang.chat.business.entity.PrivateMessageCollection;

/**
 * 
 * @author zhaodx
 *
 */
public interface PrivateMessageService
{
	public long sendMessage(PrivateMessage model) throws Exception;
	
	public PrivateMessage getPushNotify(long fromUserId, long toUserId) throws Exception;
	
	public List<PrivateMessage> getPullNotify(long userId) throws Exception;
	
	public PrivateMessageCollection getPullMessages(long fromUserId, long toUserId, long minMessageId, long maxMessageId, int pageSize) throws Exception;
}