package com.mofang.chat.business.service.impl;

import org.json.JSONObject;

import com.mofang.chat.business.entity.FriendMessage;
import com.mofang.chat.business.redis.UserRedis;
import com.mofang.chat.business.redis.WriteQueueRedis;
import com.mofang.chat.business.redis.impl.UserRedisImpl;
import com.mofang.chat.business.redis.impl.WriteQueueRedisImpl;
import com.mofang.chat.business.service.FriendMessageService;
import com.mofang.chat.business.sysconf.common.WriteDataType;

/**
 * 
 * @author zhaodx
 *
 */
public class FriendMessageServiceImpl implements FriendMessageService
{
	private final static FriendMessageServiceImpl SERVICE = new FriendMessageServiceImpl();
	private UserRedis userRedis = UserRedisImpl.getInstance();
	private WriteQueueRedis writeQueue = WriteQueueRedisImpl.getInstance();
	
	private FriendMessageServiceImpl()
	{}
	
	public static FriendMessageServiceImpl getInstance()
	{
		return SERVICE;
	}
	
	@Override
	public boolean sendMessage(FriendMessage model) throws Exception
	{
		JSONObject json = model.toJson();
		if(null == json)
			return false;
		
		json.put("write_data_type", WriteDataType.FRIEND_MESSAGE);
		return writeQueue.put(json.toString());
	}

	@Override
	public FriendMessage getPushNotify(long fromUserId, long toUserId) throws Exception
	{
		FriendMessage message = userRedis.getFriendNotify(fromUserId, toUserId);
		///将好友申请/处理结果通知移除
		userRedis.removeFriendNotify(fromUserId, toUserId);
		return message;
	}
}