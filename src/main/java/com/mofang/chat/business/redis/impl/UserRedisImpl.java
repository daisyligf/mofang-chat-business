package com.mofang.chat.business.redis.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.entity.Atom;
import com.mofang.chat.business.entity.FriendMessage;
import com.mofang.chat.business.entity.PrivateMessage;
import com.mofang.chat.business.entity.User;
import com.mofang.chat.business.redis.UserRedis;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.chat.business.sysconf.SysRedisKey;
import com.mofang.framework.data.redis.RedisWorker;
import com.mofang.framework.data.redis.workers.DeleteWorker;
import com.mofang.framework.data.redis.workers.ExistsWorker;
import com.mofang.framework.data.redis.workers.ExpireWorker;
import com.mofang.framework.data.redis.workers.GetWorker;
import com.mofang.framework.data.redis.workers.SetWorker;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class UserRedisImpl implements UserRedis
{
	private final static UserRedisImpl REDIS = new UserRedisImpl();
	
	private UserRedisImpl()
	{}
	
	public static UserRedisImpl getInstance()
	{
		return REDIS;
	}

	@Override
	public boolean saveInfo(final User user, final int expireSeconds) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.USER_INFO_KEY_PREFIX + user.getUserId();
				JSONObject json = user.toJson();
				jedis.set(key, json.toString());
				jedis.expire(key, expireSeconds);
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean removeInfo(long userId) throws Exception
	{
		String key = SysRedisKey.USER_INFO_KEY_PREFIX + userId;
		RedisWorker<Boolean> worker = new DeleteWorker(key);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public User getInfo(final long userId) throws Exception
	{
		RedisWorker<User> worker = new RedisWorker<User>()
		{
			@Override
			public User execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.USER_INFO_KEY_PREFIX + userId;
				if(!jedis.exists(key))
					return null;
				
				String value = jedis.get(key);
				if(StringUtil.isNullOrEmpty(value))
					return null;
				
				JSONObject json = new JSONObject(value);
				return User.buildByJson(json);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
	
	@Override
	public boolean setAllowSend(long fromUserId, long toUserId, int expireSeconds) throws Exception
	{
		String key = SysRedisKey.USER_ALLOW_SEND_TOUID_KEY_PREFIX + fromUserId + "_" + toUserId;
		RedisWorker<Boolean> worker = new SetWorker(key, "true");
		SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
		worker = new ExpireWorker(key, expireSeconds);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}
	
	public boolean getAllowSend(long fromUserId, long toUserId) throws Exception
	{
		String key = SysRedisKey.USER_ALLOW_SEND_TOUID_KEY_PREFIX + fromUserId + "_" + toUserId;
		RedisWorker<String> worker = new GetWorker(key);
		
		String value = SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
		if(StringUtil.isNullOrEmpty(value))
			return false;
		return "true".equals(value);
	}

	@Override
	public boolean setFrontend(final long userId, final String serverIp) throws Exception
	{
		String key = SysRedisKey.UID_FRONTEND_KEY_PREFIX + userId;
		RedisWorker<Boolean> worker = new SetWorker(key, serverIp);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean removeFrontend(final long userId) throws Exception
	{
		String key = SysRedisKey.UID_FRONTEND_KEY_PREFIX + userId;
		RedisWorker<Boolean> worker = new DeleteWorker(key);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public String getFrontend(final long userId) throws Exception 
	{
		String key = SysRedisKey.UID_FRONTEND_KEY_PREFIX + userId;
		RedisWorker<String> worker = new GetWorker(key);
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public boolean incrUnreadCount(final PrivateMessage model) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_PRIVATE_UNREAD_KEY_PREFIX + model.getToUserId();
				String message = jedis.hget(key, String.valueOf(model.getFromUserId()));
				model.setUnreadCount(1);
				if(!StringUtil.isNullOrEmpty(message))
				{
					PrivateMessage oldModel = PrivateMessage.buildByJson(new JSONObject(message));
					if(null != oldModel)
						model.setUnreadCount(oldModel.getUnreadCount() + 1);
				}
				JSONObject json = model.toJson();
				jedis.hset(key, String.valueOf(model.getFromUserId()), json.toString());
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean removeUnreadCount(final long fromUserId, final long toUserId) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_PRIVATE_UNREAD_KEY_PREFIX + toUserId;
				jedis.hdel(key, String.valueOf(fromUserId));
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public PrivateMessage getUnreadCount(final long fromUserId, final long toUserId) throws Exception 
	{
		RedisWorker<PrivateMessage> worker = new RedisWorker<PrivateMessage>()
		{
			@Override
			public PrivateMessage execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_PRIVATE_UNREAD_KEY_PREFIX + toUserId;
				String message = jedis.hget(key, String.valueOf(fromUserId));
				if(StringUtil.isNullOrEmpty(message))
					return null;
				
				return PrivateMessage.buildByJson(new JSONObject(message));
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public Map<Long, PrivateMessage> getUnreadCounts(final long userId) throws Exception
	{
		RedisWorker<Map<Long, PrivateMessage>> worker = new RedisWorker<Map<Long, PrivateMessage>>()
		{
			@Override
			public Map<Long, PrivateMessage> execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_PRIVATE_UNREAD_KEY_PREFIX + userId;
				Map<String, String> map = jedis.hgetAll(key);
				if(null == map || map.size() == 0)
					return null;
				
				Iterator<String> iterator = map.keySet().iterator();
				Map<Long, PrivateMessage> privateMap = new HashMap<Long, PrivateMessage>();
				PrivateMessage model = null;
				while(iterator.hasNext())
				{
					String fromUserId = (String)iterator.next(); 
					String message = map.get(fromUserId);
					if(StringUtil.isNullOrEmpty(message))
						continue;
						
					model = PrivateMessage.buildByJson(new JSONObject(message));
					privateMap.put(Long.parseLong(fromUserId), model);
				}
				return privateMap;
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
	
	@Override
	public boolean setFriendNotify(final FriendMessage model) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_FRIEDN_NOTIFY_KEY_PREFIX + model.getToUserId();
				JSONObject json = model.toJson();
				jedis.hset(key, String.valueOf(model.getFromUserId()), json.toString());
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean removeFriendNotify(final long fromUserId, final long toUserId) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_FRIEDN_NOTIFY_KEY_PREFIX + toUserId;
				jedis.hdel(key, String.valueOf(fromUserId));
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public FriendMessage getFriendNotify(final long fromUserId, final long toUserId) throws Exception
	{
		RedisWorker<FriendMessage> worker = new RedisWorker<FriendMessage>()
		{
			@Override
			public FriendMessage execute(Jedis jedis) throws Exception 
			{
				String key = SysRedisKey.USER_FRIEDN_NOTIFY_KEY_PREFIX + toUserId;
				String message = jedis.hget(key, String.valueOf(fromUserId));
				if(StringUtil.isNullOrEmpty(message))
					return null;
				
				return FriendMessage.buildByJson(new JSONObject(message));
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public boolean setAtom(final Atom atom) throws Exception
	{
		String key = SysRedisKey.USER_ATOM_KEY_PREFIX + atom.getUserId();
		JSONObject json =  atom.toJson();
		RedisWorker<Boolean> worker = new SetWorker(key, json.toString());
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean removeAtom(long userId) throws Exception
	{
		String key = SysRedisKey.USER_ATOM_KEY_PREFIX + userId;
		RedisWorker<Boolean> worker = new DeleteWorker(key);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public Atom getAtom(final long userId) throws Exception
	{
		RedisWorker<Atom> worker = new RedisWorker<Atom>()
		{
			@Override
			public Atom execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.USER_ATOM_KEY_PREFIX + userId;
				if(!jedis.exists(key))
					return null;
				
				String value = jedis.get(key);
				if(StringUtil.isNullOrEmpty(value))
					return null;
				
				JSONObject json = new JSONObject(value);
				return Atom.buildByJson(json);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public boolean setToken(long userId, String token) throws Exception
	{
		String key = SysRedisKey.USER_TOKEN_KEY_PREFIX + userId;
		RedisWorker<Boolean> worker = new SetWorker(key, token);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean removeToken(long userId) throws Exception
	{
		String key = SysRedisKey.USER_TOKEN_KEY_PREFIX + userId;
		RedisWorker<Boolean> worker = new DeleteWorker(key);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public String getToken(long userId) throws Exception
	{
		String key = SysRedisKey.USER_TOKEN_KEY_PREFIX + userId;
		RedisWorker<String> worker = new GetWorker(key);
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
}