package com.mofang.chat.business.mysql.impl;

import java.util.List;

import com.mofang.chat.business.model.ChatPrivateMessage;
import com.mofang.chat.business.mysql.ChatPrivateMessageDao;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.framework.data.mysql.AbstractMysqlSupport;
import com.mofang.framework.data.mysql.core.criterion.operand.AndOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.BracketOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.EqualOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.GreaterThanOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.LessThanOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.LimitOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.Operand;
import com.mofang.framework.data.mysql.core.criterion.operand.OrOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.OrderByEntry;
import com.mofang.framework.data.mysql.core.criterion.operand.OrderByOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.WhereOperand;
import com.mofang.framework.data.mysql.core.criterion.type.BracketType;
import com.mofang.framework.data.mysql.core.criterion.type.SortType;
import com.mofang.framework.data.mysql.core.meta.ResultData;
import com.mofang.framework.data.mysql.core.meta.RowData;

/**
 * 
 * @author zhaodx
 *
 */
public class ChatPrivateMessageDaoImpl extends AbstractMysqlSupport<ChatPrivateMessage> implements ChatPrivateMessageDao
{
	private final static ChatPrivateMessageDaoImpl DAO = new ChatPrivateMessageDaoImpl();
	
	private ChatPrivateMessageDaoImpl()
	{
		try
		{
			super.setMysqlPool(SysObject.MYSQL_CONNECTION_POOL);
		}
		catch(Exception e)
		{}
	}
	
	public static ChatPrivateMessageDaoImpl getInstance()
	{
		return DAO;
	}

	@Override
	public boolean add(ChatPrivateMessage model) throws Exception
	{
		return super.insert(model);
	}
	
	@Override
	public Long getMinId() throws Exception
	{
		StringBuilder strSql = new StringBuilder();
		strSql.append("select min(message_id) from chat_private_message");
		ResultData data = super.executeQuery(strSql.toString());
		if(null == data)
			return 0L;
		
		List<RowData> rows = data.getQueryResult();
		if(null == rows || rows.size() == 0)
			return 0L;
		
		RowData row = rows.get(0);
		return row.getLong(0);
	}

	@Override
	public ChatPrivateMessage getInfo(long messageId) throws Exception
	{
		return super.getByPrimaryKey(messageId);
	}

	@Override
	public List<ChatPrivateMessage> getMessages(long userId, long toUserId, long minMessageId, long maxMessageId, int count) throws Exception
	{
		Operand where = new WhereOperand();
		Operand and = new AndOperand();
		Operand or = new OrOperand();
		Operand equal = new EqualOperand("user_id", userId);
		Operand equal2 = new EqualOperand("to_user_id", toUserId);
		Operand equal3 = new EqualOperand("user_id", toUserId);
		Operand equal4 = new EqualOperand("to_user_id", userId);
		
		Operand leftBracket = new BracketOperand(BracketType.Left);
		Operand rightBracket = new BracketOperand(BracketType.Right);
		
		Operand greaterThan = new GreaterThanOperand("message_id", minMessageId);
		Operand lesserThan = new LessThanOperand("message_id", maxMessageId);
		Operand orderby = new OrderByOperand(new OrderByEntry("message_id", SortType.Desc));
		Operand limit = new LimitOperand(0L, Long.parseLong(String.valueOf(count)));
		
		where = where.append(leftBracket).append(leftBracket).append(equal).append(and).append(equal2).append(rightBracket);
		where = where.append(or).append(leftBracket).append(equal3).append(and).append(equal4).append(rightBracket).append(rightBracket);
		where = where.append(and).append(greaterThan).append(and).append(lesserThan).append(orderby).append(limit);
		return super.getList(where);
	}
}