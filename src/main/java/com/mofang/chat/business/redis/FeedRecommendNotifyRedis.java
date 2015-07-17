package com.mofang.chat.business.redis;

import java.util.Set;

import com.mofang.chat.business.model.FeedRecommendNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface FeedRecommendNotifyRedis
{
	/**
	 * 获取Feed点赞通知最大ID
	 * @return
	 * @throws Exception
	 */
	public long getMaxNotifyId() throws Exception;
	
	/**
	 * 保存Feed点赞通知
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean save(FeedRecommendNotify model) throws Exception;
	
	/**
	 * 删除用户的Feed点赞通知
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteByUser(long userId) throws Exception;
	
	/**
	 * 获取Feed点赞通知
	 * @param notifyId
	 * @return
	 */
	public FeedRecommendNotify getInfo(long notifyId);
	
	/**
	 * 获取Feed点赞通知列表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Set<String> getList(long userId) throws Exception;
	
	/**
	 * 获取未读点赞通知总数
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public long getCount(long userId) throws Exception;
}