package com.mofang.chat.business.mysql;

import com.mofang.chat.business.model.SysMessageNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface SysMessageNotifyDao
{
	public boolean add(SysMessageNotify model) throws Exception;
}