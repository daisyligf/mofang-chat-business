package com.mofang.chat.business.mysql.impl;

import com.mofang.chat.business.model.ChatFriendMessage;
import com.mofang.chat.business.mysql.ChatFriendMessageDao;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.framework.data.mysql.AbstractMysqlSupport;

/**
 * 
 * @author zhaodx
 *
 */
public class ChatFriendMessageDaoImpl extends AbstractMysqlSupport<ChatFriendMessage> implements ChatFriendMessageDao
{
	private final static ChatFriendMessageDaoImpl DAO = new ChatFriendMessageDaoImpl();
	
	private ChatFriendMessageDaoImpl()
	{
		try
		{
			super.setMysqlPool(SysObject.MYSQL_CONNECTION_POOL);
		}
		catch(Exception e)
		{}
	}
	
	public static ChatFriendMessageDaoImpl getInstance()
	{
		return DAO;
	}

	@Override
	public boolean add(ChatFriendMessage model) throws Exception
	{
		return super.insert(model);
	}
}