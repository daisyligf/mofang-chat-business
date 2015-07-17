package com.mofang.chat.business.redis.impl;

import java.util.Set;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.mofang.chat.business.model.FeedRecommendNotify;
import com.mofang.chat.business.redis.FeedRecommendNotifyRedis;
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
public class FeedRecommendNotifyRedisImpl implements FeedRecommendNotifyRedis
{
	private final static FeedRecommendNotifyRedisImpl REDIS = new FeedRecommendNotifyRedisImpl();
	
	private FeedRecommendNotifyRedisImpl()
	{}
	
	public static FeedRecommendNotifyRedisImpl getInstance()
	{
		return REDIS;
	}

	@Override
	public long getMaxNotifyId() throws Exception
	{
		RedisWorker<Long> worker = new IncrWorker(SysRedisKey.FEED_RECOMMEND_NOTIFY_ID_NCREMENT_KEY);
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean save(final FeedRecommendNotify model) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String infoKey = SysRedisKey.FEED_RECOMMEND_NOTIFY_INFO_KEY_PREFIX + model.getNotifyId();
				String listKey = SysRedisKey.USER_FEED_RECOMMEND_NOTIFY_LIST_KEY_PREFIX + model.getUserId();
				
				///将系统消息通知信息添加到redis中
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
				///将feed点赞通知信息从redis中删除
				Set<String> notifyIds = getList(userId);
				if(null != notifyIds)
				{
					String infoKey = null;
					for(String notifyId : notifyIds)
					{
						infoKey = SysRedisKey.FEED_RECOMMEND_NOTIFY_INFO_KEY_PREFIX + notifyId;
						jedis.del(infoKey);
					}
				}
				
				///将用户对应的feed点赞通知列表从redis中删除
				String listKey = SysRedisKey.USER_FEED_RECOMMEND_NOTIFY_LIST_KEY_PREFIX + userId;
				jedis.del(listKey);
				return true;
			}
		};
		return SysObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public FeedRecommendNotify getInfo(final long notifyId) 
	{
		RedisWorker<FeedRecommendNotify> worker = new RedisWorker<FeedRecommendNotify>()
		{
			@Override
			public FeedRecommendNotify execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.FEED_RECOMMEND_NOTIFY_INFO_KEY_PREFIX + notifyId;
				String value = jedis.get(key);
				if(StringUtil.isNullOrEmpty(value))
					return null;
				
				JSONObject json = new JSONObject(value);
				return FeedRecommendNotify.buildByJson(json);
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
				String key = SysRedisKey.USER_FEED_RECOMMEND_NOTIFY_LIST_KEY_PREFIX + userId;
				return jedis.zrevrange(key, 0, -1);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public long getCount(final long userId) throws Exception
	{
		RedisWorker<Long> worker = new RedisWorker<Long>()
		{
			@Override
			public Long execute(Jedis jedis) throws Exception
			{
				String key = SysRedisKey.USER_FEED_RECOMMEND_NOTIFY_LIST_KEY_PREFIX + userId;
				return jedis.zcard(key);
			}
		};
		return SysObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	} 
}