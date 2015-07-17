package com.mofang.chat.business.service;

import java.util.List;

import com.mofang.chat.business.model.FeedRecommendNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface FeedRecommendNotifyService
{
	public void pushNotify(FeedRecommendNotify model) throws Exception;
	
	public FeedRecommendNotify getInfo(long notifyId) throws Exception;
	
	public List<FeedRecommendNotify> getList(long userId) throws Exception;
	
	public List<FeedRecommendNotify> getList(long userId, long start, long size) throws Exception;
	
	public long getCount(long userId) throws Exception;
	
	public long getUnreadCount(long userId) throws Exception;
}