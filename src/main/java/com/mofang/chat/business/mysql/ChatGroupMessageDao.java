package com.mofang.chat.business.mysql;

import java.util.List;

import com.mofang.chat.business.model.ChatGroupMessage;

/**
 * 
 * @author zhaodx
 *
 */
public interface ChatGroupMessageDao
{
	public boolean add(ChatGroupMessage model) throws Exception;
	
	public Long getMinId() throws Exception;
	
	public ChatGroupMessage getInfo(long messageId) throws Exception;
	
	public List<ChatGroupMessage> getMessages(long groupId, long minMessageId, long maxMessageId, int count) throws Exception;
	
	public boolean updateStatus(long messageId, int status) throws Exception;
}