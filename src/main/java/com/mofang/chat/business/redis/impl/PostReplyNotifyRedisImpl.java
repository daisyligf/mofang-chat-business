package com.mofang.chat.business.redis.impl;

import java.util.Set;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.model.PostReplyNotify;
import com.mofang.chat.business.redis.PostReplyNotifyRedis;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.chat.business.sysconf.SysRedisKey;
import com.mofang.framework.data.redis.RedisWorker;
import com.mofang.framework.data.redis.workers.IncrWorker;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class PostReplyNotifyRedisImpl implements PostReplyNotifyRedis
{
	private final static PostReplyNotifyRedisImpl REDIS = new PostReplyNotifyRedisImpl();
	
	private PostReplyNotifyRedisImpl()
	{}
	
	public static PostReplyNotifyRedisImpl getInstance()
	{
		return REDIS;
	}

	@Override
	public long getMaxNotifyId() throws Exception
	{
		RedisWorker<Long> worker = new IncrWorker(SysRedisKey.POST_REPLY_NOTIFY_ID_NCREMENT_KEY);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean save(final PostReplyNotify model) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String infoKey = SysRedisKey.POST_REPLY_NOTIFY_INFO_KEY_PREFIX + model.getNotifyId();
				String listKey = SysRedisKey.USER_POST_REPLY_NOTIFY_LIST_KEY_PREFIX + model.getUserId();
				
				///将帖子回复通知信息添加到redis中
				JSONObject json = model.toJson();
				jedis.set(infoKey, json.toString());
				
				///将notifyId添加到userId对应的通知列表中
				jedis.zadd(listKey, model.getNotifyId().doubleValue(), model.getNotifyId().toString());
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean deleteByUser(final long userId) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				///将帖子回复通知信息从redis中删除
				Set<String> notifyIds = getList(userId);
				if(null != notifyIds)
				{
					String infoKey = null;
					for(String notifyId : notifyIds)
					{
						infoKey = SysRedisKey.POST_REPLY_NOTIFY_INFO_KEY_PREFIX + notifyId;
						jedis.del(infoKey);
					}
				}
				
				///将用户对应的帖子回复通知列表从redis中删除
				String listKey = SysRedisKey.USER_POST_REPLY_NOTIFY_LIST_KEY_PREFIX + userId;
				jedis.del(listKey);
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public PostReplyNotify getInfo(final long notifyId)
	{
		RedisWorker<PostReplyNotify> worker = new RedisWorker<PostReplyNotify>()
		{
			@Override
			public PostReplyNotify execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.POST_REPLY_NOTIFY_INFO_KEY_PREFIX + notifyId;
				String value = jedis.get(key);
				if(StringUtil.isNullOrEmpty(value))
					return null;
				
				JSONObject json = new JSONObject(value);
				return PostReplyNotify.buildByJson(json);
			}
		};
		try
		{
			return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Override
	public Set<String> getList(final long userId) throws Exception
	{
		RedisWorker<Set<String>> worker = new RedisWorker<Set<String>>()
		{
			@Override
			public Set<String> execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.USER_POST_REPLY_NOTIFY_LIST_KEY_PREFIX + userId;
				return jedis.zrevrange(key, 0, Integer.MAX_VALUE);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
}