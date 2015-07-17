package com.mofang.chat.business.redis;

import java.util.Set;

import com.mofang.chat.business.model.SysMessageNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface SysMessageNotifyRedis
{
	/**
	 * 获取系统消息最大ID
	 * @return
	 * @throws Exception
	 */
	public long getMaxNotifyId() throws Exception;
	
	/**
	 * 保存系统消息通知
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean save(SysMessageNotify model) throws Exception;
	
	/**
	 * 删除用户的系统消息通知
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteByUser(long userId) throws Exception;
	
	/**
	 * 获取系统消息通知
	 * @param notifyId
	 * @return
	 */
	public SysMessageNotify getInfo(long notifyId);
	
	/**
	 * 获取系统消息通知列表 <br />
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Set<String> getList(long userId) throws Exception;
	
	/**
	 * 获取未读通知总数
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public long getCount(long userId) throws Exception;
}