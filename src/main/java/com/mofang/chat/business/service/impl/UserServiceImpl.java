package com.mofang.chat.business.service.impl;

import com.mofang.chat.business.component.UserComponent;
import com.mofang.chat.business.entity.User;
import com.mofang.chat.business.redis.UserRedis;
import com.mofang.chat.business.redis.impl.UserRedisImpl;
import com.mofang.chat.business.service.UserService;
import com.mofang.chat.business.sysconf.common.UserStatus;

/**
 * 
 * @author zhaodx
 *
 */
public class UserServiceImpl implements UserService
{
	private final static UserServiceImpl SERVICE = new UserServiceImpl();
	private UserRedis userRedis = UserRedisImpl.getInstance();
	
	private UserServiceImpl()
	{}
	
	public static UserServiceImpl getInstance()
	{
		return SERVICE;
	}
	
	@Override
	public User getInfo(long userId) throws Exception
	{
		///先从本地获取用户信息, 如果本地没有缓存，则调用服务端接口获取
		User user = userRedis.getInfo(userId);
		if(null != user)
			return user;
		
		/// 调用服务端接口获取
		user = UserComponent.getInfo(userId);
		if(null == user)
			return null;
		
		try
		{
			///保存到redis中
			userRedis.saveInfo(user, 300);
			return user;
		}
		catch(Exception e)
		{	
			throw e;
		}
	}

	@Override
	public User getAuth(long userId, String sessionId) throws Exception
	{
		///先从本地获取用户信息, 如果本地没有缓存，则调用服务端接口获取
		User user = userRedis.getInfo(userId);
		if(null != user)
			return user;
		
		/// 调用服务端接口获取
		user = UserComponent.getAuth(userId, sessionId);
		if(null == user)
			return null;
		
		try
		{
			///保存到redis中(预注册用户不存入redis)
			if(user.getStatus() != UserStatus.PREREGISTER)
				userRedis.saveInfo(user, 300);
			return user;
		}
		catch(Exception e)
		{	
			throw e;
		}
	}

	@Override
	public boolean allowSendToUser(long fromUserId, long toUserId)  throws Exception
	{
		boolean isAllow = userRedis.getAllowSend(fromUserId, toUserId);
		if(isAllow)
			return true;
		
		///从服务端获取权限
		isAllow = UserComponent.isAllowToSend(fromUserId, toUserId);
		if(isAllow)///保存到redis中
			userRedis.setAllowSend(fromUserId, toUserId, 300);
		return isAllow;
	}
}