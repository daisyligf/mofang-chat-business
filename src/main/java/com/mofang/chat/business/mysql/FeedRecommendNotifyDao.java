package com.mofang.chat.business.mysql;

import java.util.List;

import com.mofang.chat.business.model.FeedRecommendNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface FeedRecommendNotifyDao
{
	public boolean add(FeedRecommendNotify model) throws Exception;
	
	public void updateStatusByUserId(long userId, int status) throws Exception;
	
	public List<FeedRecommendNotify> getList(long userId, long start, long size) throws Exception;
	
	public long getCount(long userId) throws Exception;
	
	public long getCount(long userId, int status) throws Exception;
}