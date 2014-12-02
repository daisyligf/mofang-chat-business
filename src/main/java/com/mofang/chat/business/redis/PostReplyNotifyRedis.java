package com.mofang.chat.business.redis;

import java.util.Set;

import com.mofang.chat.business.model.PostReplyNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface PostReplyNotifyRedis
{
	/**
	 * 获取帖子通知最大ID
	 * @return
	 * @throws Exception
	 */
	public long getMaxNotifyId() throws Exception;
	
	/**
	 * 保存帖子回复通知
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean save(PostReplyNotify model) throws Exception;
	
	/**
	 * 删除用户的帖子回复通知
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteByUser(long userId) throws Exception;
	
	/**
	 * 获取帖子回复通知
	 * @param notifyId
	 * @return
	 */
	public PostReplyNotify getInfo(long notifyId);
	
	/**
	 * 获取帖子回复通知列表 <br />
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Set<String> getList(long userId) throws Exception;
}