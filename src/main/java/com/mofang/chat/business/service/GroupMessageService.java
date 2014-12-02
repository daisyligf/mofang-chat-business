package com.mofang.chat.business.service;

import java.util.List;

import com.mofang.chat.business.entity.GroupMessage;
import com.mofang.chat.business.entity.GroupMessageCollection;

/**
 * 
 * @author zhaodx
 *
 */
public interface GroupMessageService
{
	public long sendMessage(GroupMessage model) throws Exception;
	
	public GroupMessage getNotifyInfo(long userId, long groupId) throws Exception;
	
	public List<GroupMessage> getNotifyList(long userId) throws Exception;
	
	public GroupMessageCollection getMessages(long userId, long groupId, long minMessageId, long maxMessageId, int pageSize) throws Exception;
	
	public boolean updateStatus(long messageId, int status) throws Exception;
}