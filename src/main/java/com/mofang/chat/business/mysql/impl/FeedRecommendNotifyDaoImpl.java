package com.mofang.chat.business.mysql.impl;

import java.util.List;

import com.mofang.chat.business.model.FeedRecommendNotify;
import com.mofang.chat.business.mysql.FeedRecommendNotifyDao;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.framework.data.mysql.AbstractMysqlSupport;
import com.mofang.framework.data.mysql.core.criterion.operand.AndOperand;
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
public class FeedRecommendNotifyDaoImpl extends AbstractMysqlSupport<FeedRecommendNotify> implements FeedRecommendNotifyDao
{
	private final static FeedRecommendNotifyDaoImpl DAO = new FeedRecommendNotifyDaoImpl();
	
	private FeedRecommendNotifyDaoImpl()
	{
		try
		{
			super.setMysqlPool(SysObject.MYSQL_CONNECTION_POOL);
		}
		catch(Exception e)
		{}
	}
	
	public static FeedRecommendNotifyDaoImpl getInstance()
	{
		return DAO;
	}

	@Override
	public boolean add(FeedRecommendNotify model) throws Exception
	{
		return super.insert(model);
	}

	@Override
	public void updateStatusByUserId(long userId, int status) throws Exception
	{
		StringBuilder strSql = new StringBuilder();
		strSql.append("update feed_recommend_notify set status=" + status + " where user_id=" + userId);
		super.invokeExecute(strSql.toString());
	}

	@Override
	public List<FeedRecommendNotify> getList(long userId, long start, long size) throws Exception
	{
		Operand where = new WhereOperand();
		Operand userEqual = new EqualOperand("user_id", userId);
		OrderByEntry entry = new OrderByEntry("notify_id", SortType.Desc);
		Operand orderby = new OrderByOperand(entry);
		Operand limit = new LimitOperand(start, size);
		where.append(userEqual).append(orderby).append(limit);
		return super.getList(where);
	}

	@Override
	public long getCount(long userId) throws Exception
	{
		Operand where = new WhereOperand();
		Operand userEqual = new EqualOperand("user_id", userId);
		where.append(userEqual);
		return super.getCount(where);
	}

	@Override
	public long getCount(long userId, int status) throws Exception
	{
		Operand where = new WhereOperand();
		Operand userEqual = new EqualOperand("user_id", userId);
		Operand statusEqual = new EqualOperand("status", status);
		Operand and = new AndOperand();
		where.append(userEqual).append(and).append(statusEqual);
		return super.getCount(where);
	}
}