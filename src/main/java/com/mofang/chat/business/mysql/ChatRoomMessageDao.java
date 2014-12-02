package com.mofang.chat.business.mysql;

import java.util.List;

import com.mofang.chat.business.model.ChatRoomMessage;

/**
 * 
 * @author zhaodx
 *
 */
public interface ChatRoomMessageDao
{
	public boolean add(ChatRoomMessage model) throws Exception;
	
	public Long getMinId() throws Exception;
	
	public ChatRoomMessage getInfo(long messageId) throws Exception;
	
	public List<ChatRoomMessage> getMessages(int roomId, long minMessageId, long maxMessageId, int count) throws Exception;
	
	public boolean updateStatus(long messageId, int status) throws Exception;
}