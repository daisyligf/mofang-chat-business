package com.mofang.chat.business.service;

import java.util.List;

import com.mofang.chat.business.model.SysMessageNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface SysMessageNotifyService
{
	public void pushNotify(SysMessageNotify model) throws Exception;
	
	public SysMessageNotify getInfo(long notifyId) throws Exception;
	
	public List<SysMessageNotify> getList(long userId) throws Exception;
	
	public List<SysMessageNotify> getList(long userId, long start, long size) throws Exception;
	
	public long getCount(long userId) throws Exception;
}