package com.mofang.chat.business.mysql.impl;

import java.util.List;

import com.mofang.chat.business.model.PostReplyNotify;
import com.mofang.chat.business.mysql.PostReplyNotifyDao;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.framework.data.mysql.AbstractMysqlSupport;
import com.mofang.framework.data.mysql.core.criterion.operand.EqualOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.LimitOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.Operand;
import com.mofang.framework.data.mysql.core.criterion.operand.OrderByEntry;
import com.mofang.framework.data.mysql.core.criterion.operand.OrderByOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.WhereOperand;
import com.mofang.framework.data.mysql.core.criterion.type.SortType;

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

	@Override
	public void updateStatusByUserId(long userId, int status) throws Exception
	{
		StringBuilder strSql = new StringBuilder();
		strSql.append("update post_reply_notify set status=" + status + " where user_id=" + userId);
		super.invokeExecute(strSql.toString());
	}

	@Override
	public List<PostReplyNotify> getList(long userId, long start, long size) throws Exception
	{
		Operand where = new WhereOperand();
		Operand userEqual = new EqualOperand("user_id", userId);
		OrderByEntry entry = new OrderByEntry("notify_id", SortType.Desc);
		Operand orderby = new OrderByOperand(entry);
		Operand limit = new LimitOperand(start, size);
		where.append(userEqual).append(orderby).append(limit);
		return super.getList(where);
	}
}