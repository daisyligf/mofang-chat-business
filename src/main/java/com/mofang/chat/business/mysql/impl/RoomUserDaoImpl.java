package com.mofang.chat.business.mysql.impl;

import java.util.ArrayList;
import java.util.List;

import com.mofang.chat.business.model.RoomUser;
import com.mofang.chat.business.mysql.RoomUserDao;
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
import com.mofang.framework.data.mysql.core.meta.ResultData;
import com.mofang.framework.data.mysql.core.meta.RowData;

/**
 * 
 * @author zhaodx
 *
 */
public class RoomUserDaoImpl extends AbstractMysqlSupport<RoomUser> implements RoomUserDao
{
	private final static RoomUserDaoImpl DAO = new RoomUserDaoImpl();
	
	private RoomUserDaoImpl()
	{
		try
		{
			super.setMysqlPool(SysObject.MYSQL_CONNECTION_POOL);
		}
		catch(Exception e)
		{}
	}
	
	public static RoomUserDaoImpl getInstance()
	{
		return DAO;
	}

	@Override
	public boolean add(RoomUser model) throws Exception
	{
		return super.insert(model);
	}

	@Override
	public boolean updateMsgCount(RoomUser model) throws Exception
	{
		Operand where = new WhereOperand();
		Operand equal = new EqualOperand("room_id", model.getRoomId());
		Operand and = new AndOperand();
		Operand equal2 = new EqualOperand("user_id", model.getUserId());
		where.append(equal).append(and).append(equal2);
		return super.updateByWhere(model, where);
	}

	@Override
	public boolean delete(int roomId, long userId) throws Exception
	{
		Operand where = new WhereOperand();
		Operand equal = new EqualOperand("room_id", roomId);
		Operand and = new AndOperand();
		Operand equal2 = new EqualOperand("user_id", userId);
		where.append(equal).append(and).append(equal2);
		return super.deleteByWhere(where);
	}

	@Override
	public List<RoomUser> getUsers(int roomId) throws Exception
	{
		Operand where = new WhereOperand();
		Operand equal = new EqualOperand("room_id", roomId);
		where.append(equal);
		return super.getList(where);
	}

	@Override
	public List<RoomUser> getUsersOrderByCount(int roomId, long limit) throws Exception
	{
		Operand where = new WhereOperand();
		Operand equal = new EqualOperand("room_id", roomId);
		OrderByEntry entry = new OrderByEntry("msg_count", SortType.Desc);
		Operand orderby = new OrderByOperand(entry);
		Operand limited = new LimitOperand(0L, limit);
		where.append(equal).append(orderby).append(limited);
		return super.getList(where);
	}

	@Override
	public List<RoomUser> getRooms(long userId) throws Exception
	{
		Operand where = new WhereOperand();
		Operand equal = new EqualOperand("user_id", userId);
		where.append(equal);
		return super.getList(where);
	}

	@Override
	public List<Integer> getAllRooms() throws Exception
	{
		StringBuilder strSql = new StringBuilder();
		strSql.append("select distinct room_id from room_user ");
		ResultData result = super.executeQuery(strSql.toString());
		if(null == result)
			return null;
		
		List<RowData> rows = result.getQueryResult();
		if(null == rows || rows.size() == 0)
			return null;
		
		List<Integer> roomIds = new ArrayList<Integer>();
		for(RowData row : rows)
			roomIds.add(row.getInteger(0));
		return roomIds;
	}
}