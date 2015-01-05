package com.mofang.chat.business.mysql;

import java.util.List;

import com.mofang.chat.business.model.SysMessageNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface SysMessageNotifyDao
{
	public boolean add(SysMessageNotify model) throws Exception;
	
	public void updateStatusByUserId(long userId, int status) throws Exception;
	
	public List<SysMessageNotify> getList(long userId, long start, long size) throws Exception;
}