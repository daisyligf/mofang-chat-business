package com.mofang.chat.business.mysql;

import com.mofang.chat.business.model.ChatFriendMessage;

/**
 * 
 * @author zhaodx
 *
 */
public interface ChatFriendMessageDao
{
	public boolean add(ChatFriendMessage model) throws Exception;
}