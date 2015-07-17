package com.mofang.chat.business.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.mofang.chat.business.model.FeedRecommendNotify;
import com.mofang.chat.business.mysql.FeedRecommendNotifyDao;
import com.mofang.chat.business.mysql.impl.FeedRecommendNotifyDaoImpl;
import com.mofang.chat.business.redis.FeedRecommendNotifyRedis;
import com.mofang.chat.business.redis.WriteQueueRedis;
import com.mofang.chat.business.redis.impl.FeedRecommendNotifyRedisImpl;
import com.mofang.chat.business.redis.impl.WriteQueueRedisImpl;
import com.mofang.chat.business.service.FeedRecommendNotifyService;
import com.mofang.chat.business.sysconf.common.FeedRecommendNotifyStatus;
import com.mofang.chat.business.sysconf.common.WriteDataType;

/**
 * 
 * @author zhaodx
 *
 */
public class FeedRecommendNotifyServiceImpl implements FeedRecommendNotifyService
{
	private final static FeedRecommendNotifyServiceImpl SERVICE = new FeedRecommendNotifyServiceImpl();
	private FeedRecommendNotifyRedis feedRecommendNotifyRedis = FeedRecommendNotifyRedisImpl.getInstance();
	private FeedRecommendNotifyDao feedRecommendNotifyDao = FeedRecommendNotifyDaoImpl.getInstance();
	private WriteQueueRedis writeQueue = WriteQueueRedisImpl.getInstance();
	
	private FeedRecommendNotifyServiceImpl()
	{}
	
	public static FeedRecommendNotifyServiceImpl getInstance()
	{
		return SERVICE;
	}

	@Override
	public void pushNotify(FeedRecommendNotify model) throws Exception
	{
		///获取feed点赞通知最大ID
		Long notifyId = feedRecommendNotifyRedis.getMaxNotifyId();
		JSONObject json = model.toJson();
		if(null == json)
			return;
		
		json.put("notify_id", notifyId);
		json.put("write_data_type", WriteDataType.FEED_RECOMMEND);
		writeQueue.put(json.toString());
	}

	@Override
	public FeedRecommendNotify getInfo(long notifyId) throws Exception
	{
		return feedRecommendNotifyRedis.getInfo(notifyId);
	}

	@Override
	public List<FeedRecommendNotify> getList(long userId) throws Exception
	{
		Set<String> notifyIds = feedRecommendNotifyRedis.getList(userId);
		if(null == notifyIds || notifyIds.size() == 0)
			return null;
		
		Iterator<String> iterator = notifyIds.iterator();
		Long notifyId;
		List<FeedRecommendNotify> list = new ArrayList<FeedRecommendNotify>();
		while(iterator.hasNext())
		{
			notifyId = Long.parseLong(iterator.next());
			if(null == notifyId || notifyId == 0)
				continue;
			
			FeedRecommendNotify notify = getInfo(notifyId);
			if(null == notify)
				continue;
			
			list.add(notify);
		}
		
		///清空用户的Feed点赞通知列表
		feedRecommendNotifyRedis.deleteByUser(userId);
		///更新用户的Feed点赞通知状态
		feedRecommendNotifyDao.updateStatusByUserId(userId, FeedRecommendNotifyStatus.READ);
		return list;
	}

	@Override
	public List<FeedRecommendNotify> getList(long userId, long start, long size) throws Exception
	{
		///清空用户的Feed点赞通知列表
		feedRecommendNotifyRedis.deleteByUser(userId);
		///获取通知列表
		List<FeedRecommendNotify> list = feedRecommendNotifyDao.getList(userId, start, size);
		///更新用户的Feed点赞通知状态
		feedRecommendNotifyDao.updateStatusByUserId(userId, FeedRecommendNotifyStatus.READ);
		return list;
	}

	@Override
	public long getCount(long userId) throws Exception
	{
		return feedRecommendNotifyDao.getCount(userId);
	}

	@Override
	public long getUnreadCount(long userId) throws Exception
	{
		return feedRecommendNotifyRedis.getCount(userId);
	}
}