package com.mofang.chat.business.mysql.impl;

import com.mofang.chat.business.model.SysMessageNotify;
import com.mofang.chat.business.mysql.SysMessageNotifyDao;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.framework.data.mysql.AbstractMysqlSupport;

/**
 * 
 * @author zhaodx
 *
 */
public class SysMessageNotifyDaoImpl extends AbstractMysqlSupport<SysMessageNotify> implements SysMessageNotifyDao
{
	private final static SysMessageNotifyDaoImpl DAO = new SysMessageNotifyDaoImpl();
	
	private SysMessageNotifyDaoImpl()
	{
		try
		{
			super.setMysqlPool(SysObject.MYSQL_CONNECTION_POOL);
		}
		catch(Exception e)
		{}
	}
	
	public static SysMessageNotifyDaoImpl getInstance()
	{
		return DAO;
	}

	@Override
	public boolean add(SysMessageNotify model) throws Exception
	{
		return super.insert(model);
	}
}