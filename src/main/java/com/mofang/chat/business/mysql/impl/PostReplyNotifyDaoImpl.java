package com.mofang.chat.business.mysql.impl;

import com.mofang.chat.business.model.PostReplyNotify;
import com.mofang.chat.business.mysql.PostReplyNotifyDao;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.framework.data.mysql.AbstractMysqlSupport;

/**
 * 
 * @author zhaodx
 *
 */
public class PostReplyNotifyDaoImpl extends AbstractMysqlSupport<PostReplyNotify> implements PostReplyNotifyDao
{
	private final static PostReplyNotifyDaoImpl DAO = new PostReplyNotifyDaoImpl();
	
	private PostReplyNotifyDaoImpl()
	{
		try
		{
			super.setMysqlPool(SysObject.MYSQL_CONNECTION_POOL);
		}
		catch(Exception e)
		{}
	}
	
	public static PostReplyNotifyDaoImpl getInstance()
	{
		return DAO;
	}

	@Override
	public boolean add(PostReplyNotify model) throws Exception
	{
		return super.insert(model);
	}
}