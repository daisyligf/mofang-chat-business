package com.mofang.chat.business.mysql.impl;

import java.util.List;

import com.mofang.chat.business.model.ChatGroupMessage;
import com.mofang.chat.business.mysql.ChatGroupMessageDao;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.framework.data.mysql.AbstractMysqlSupport;
import com.mofang.framework.data.mysql.core.criterion.operand.AndOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.EqualOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.GreaterThanOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.LessThanOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.LimitOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.Operand;
import com.mofang.framework.data.mysql.core.criterion.operand.OrderByEntry;
import com.mofang.framework.data.mysql.core.criterion.operand.OrderByOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.WhereOperand;
import com.mofang.framework.data.mysql.core.criterion.type.SortType;
import com.mofang.framework.data.mysql.core.meta.ResultData;
import com.mofang.framework.data.mysql.core.meta.RowData;

/**
 * 
 * @author zhaodx
 *
 */
public class ChatGroupMessageDaoImpl extends AbstractMysqlSupport<ChatGroupMessage> implements ChatGroupMessageDao
{
	private final static ChatGroupMessageDaoImpl DAO = new ChatGroupMessageDaoImpl();
	
	private ChatGroupMessageDaoImpl()
	{
		try
		{
			super.setMysqlPool(SysObject.MYSQL_CONNECTION_POOL);
		}
		catch(Exception e)
		{}
	}
	
	public static ChatGroupMessageDaoImpl getInstance()
	{
		return DAO;
	}

	@Override
	public boolean add(ChatGroupMessage model) throws Exception
	{
		return super.insert(model);
	}

	@Override
	public Long getMinId() throws Exception
	{
		StringBuilder strSql = new StringBuilder();
		strSql.append("select min(message_id) from chat_group_message");
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
	public ChatGroupMessage getInfo(long messageId) throws Exception
	{
		return super.getByPrimaryKey(messageId);
	}

	@Override
	public List<ChatGroupMessage> getMessages(long groupId, long minMessageId, long maxMessageId, int count) throws Exception
	{
		Operand where = new WhereOperand();
		Operand and = new AndOperand();
		Operand equal = new EqualOperand("group_id", groupId);
		Operand greaterThan = new GreaterThanOperand("message_id", minMessageId);
		Operand lesserThan = new LessThanOperand("message_id", maxMessageId);
		Operand orderby = new OrderByOperand(new OrderByEntry("message_id", SortType.Desc));
		Operand limit = new LimitOperand(0L, Long.parseLong(String.valueOf(count)));
		where.append(equal).append(and).append(greaterThan).append(and).append(lesserThan).append(orderby).append(limit);
		return super.getList(where);
	}

	@Override
	public boolean updateStatus(long messageId, int status) throws Exception
	{
		StringBuilder strSql = new StringBuilder();
		strSql.append("update chat_group_message set status=" + status + " where message_id=" + messageId);
		return super.execute(strSql.toString());
	}
}