package com.mofang.chat.business.mysql;

import java.util.List;

import com.mofang.chat.business.model.ChatPrivateMessage;

/**
 * 
 * @author zhaodx
 *
 */
public interface ChatPrivateMessageDao
{
	public boolean add(ChatPrivateMessage model) throws Exception;
	
	public Long getMinId() throws Exception;
	
	public ChatPrivateMessage getInfo(long messageId) throws Exception;
	
	public List<ChatPrivateMessage> getMessages(long userId, long toUserId, long minMessageId, long maxMessageId, int count) throws Exception;
}