package com.mofang.chat.business.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.mofang.chat.business.model.PostReplyNotify;
import com.mofang.chat.business.mysql.PostReplyNotifyDao;
import com.mofang.chat.business.mysql.impl.PostReplyNotifyDaoImpl;
import com.mofang.chat.business.redis.PostReplyNotifyRedis;
import com.mofang.chat.business.redis.WriteQueueRedis;
import com.mofang.chat.business.redis.impl.PostReplyNotifyRedisImpl;
import com.mofang.chat.business.redis.impl.WriteQueueRedisImpl;
import com.mofang.chat.business.service.PostReplyNotifyService;
import com.mofang.chat.business.sysconf.common.PostReplyNotifyStatus;
import com.mofang.chat.business.sysconf.common.WriteDataType;

/**
 * 
 * @author zhaodx
 *
 */
public class PostReplyNotifyServiceImpl implements PostReplyNotifyService
{
	private final static PostReplyNotifyServiceImpl SERVICE = new PostReplyNotifyServiceImpl();
	private PostReplyNotifyRedis postReplyNotifyRedis = PostReplyNotifyRedisImpl.getInstance();
	private PostReplyNotifyDao postReplyNotifyDao = PostReplyNotifyDaoImpl.getInstance();
	private WriteQueueRedis writeQueue = WriteQueueRedisImpl.getInstance();
	
	private PostReplyNotifyServiceImpl()
	{}
	
	public static PostReplyNotifyServiceImpl getInstance()
	{
		return SERVICE;
	}

	@Override
	public void pushNotify(PostReplyNotify model) throws Exception
	{
		///获取帖子回复通知最大ID
		Long notifyId = postReplyNotifyRedis.getMaxNotifyId();
		JSONObject json = model.toJson();
		if(null == json)
			return;
		
		json.put("notify_id", notifyId);
		json.put("write_data_type", WriteDataType.POST_REPLY_NOTIFY);
		writeQueue.put(json.toString());
	}

	@Override
	public PostReplyNotify getInfo(long notifyId) throws Exception
	{
		return postReplyNotifyRedis.getInfo(notifyId);
	}

	@Override
	public List<PostReplyNotify> getList(long userId) throws Exception
	{
		Set<String> notifyIds = postReplyNotifyRedis.getList(userId);
		if(null == notifyIds || notifyIds.size() == 0)
			return null;
		
		Iterator<String> iterator = notifyIds.iterator();
		Long notifyId;
		List<PostReplyNotify> list = new ArrayList<PostReplyNotify>();
		while(iterator.hasNext())
		{
			notifyId = Long.parseLong(iterator.next());
			if(null == notifyId || notifyId == 0)
				continue;
			
			PostReplyNotify notify = getInfo(notifyId);
			if(null == notify)
				continue;
			
			list.add(notify);
		}
		
		///清空用户的帖子回复通知列表
		postReplyNotifyRedis.deleteByUser(userId);
		///更新用户的帖子回复通知状态
		postReplyNotifyDao.updateStatusByUserId(userId, PostReplyNotifyStatus.READ);
		return list;
	}

	@Override
	public List<PostReplyNotify> getList(long userId, long start, long size) throws Exception
	{
		return postReplyNotifyDao.getList(userId, start, size);
	}
}