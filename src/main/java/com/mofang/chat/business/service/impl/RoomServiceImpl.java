package com.mofang.chat.business.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.mofang.chat.business.entity.User;
import com.mofang.chat.business.redis.RoomRedis;
import com.mofang.chat.business.redis.WriteQueueRedis;
import com.mofang.chat.business.redis.impl.RoomRedisImpl;
import com.mofang.chat.business.redis.impl.WriteQueueRedisImpl;
import com.mofang.chat.business.service.RoomService;
import com.mofang.chat.business.service.UserService;
import com.mofang.chat.business.sysconf.common.WriteDataType;

/**
 * 
 * @author zhaodx
 *
 */
public class RoomServiceImpl implements RoomService
{
	private final static RoomServiceImpl SERVICE = new RoomServiceImpl();
	private WriteQueueRedis writeQueue = WriteQueueRedisImpl.getInstance();
	private RoomRedis roomRedis = RoomRedisImpl.getInstance();
	private UserService userService = UserServiceImpl.getInstance();
	
	private RoomServiceImpl()
	{}
	
	public static RoomServiceImpl getInstance()
	{
		return SERVICE;
	}
	
	@Override
	public boolean subscribe(long userId, Set<Integer> roomSet) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("write_data_type", WriteDataType.USER_SUBSCRIBE_ROOMS);
		json.put("uid", userId);
		json.put("rid_list", roomSet);
		
		///保存到写队列
		writeQueue.put(json.toString());
		return true;
	}

	@Override
	public List<User> getSubscribeUsers(int roomId, int start, int end) throws Exception
	{
		Set<String> uidSet = roomRedis.getSubscribeUsers(roomId, start, end);
		if(null == uidSet || uidSet.size() == 0)
			return null;

		List<User> userList = new ArrayList<User>();
		User user = null;
		long userId;
		Iterator<String> iterator = uidSet.iterator();
		while(iterator.hasNext())
		{
			userId = Long.parseLong(iterator.next());
			user = userService.getInfo(userId);
			if(null == user)
				continue;
			
			userList.add(user);
		}
		return userList;
	}
}