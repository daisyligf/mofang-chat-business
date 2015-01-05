package com.mofang.chat.business.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.mofang.chat.business.model.SysMessageNotify;
import com.mofang.chat.business.mysql.SysMessageNotifyDao;
import com.mofang.chat.business.mysql.impl.SysMessageNotifyDaoImpl;
import com.mofang.chat.business.redis.SysMessageNotifyRedis;
import com.mofang.chat.business.redis.WriteQueueRedis;
import com.mofang.chat.business.redis.impl.SysMessageNotifyRedisImpl;
import com.mofang.chat.business.redis.impl.WriteQueueRedisImpl;
import com.mofang.chat.business.service.SysMessageNotifyService;
import com.mofang.chat.business.sysconf.common.SysMessageNotifyStatus;
import com.mofang.chat.business.sysconf.common.WriteDataType;

/**
 * 
 * @author zhaodx
 *
 */
public class SysMessageNotifyServiceImpl implements SysMessageNotifyService
{
	private final static SysMessageNotifyServiceImpl SERVICE = new SysMessageNotifyServiceImpl();
	private SysMessageNotifyRedis sysMessageNotifyRedis = SysMessageNotifyRedisImpl.getInstance();
	private SysMessageNotifyDao sysMessageNotifyDao = SysMessageNotifyDaoImpl.getInstance();
	private WriteQueueRedis writeQueue = WriteQueueRedisImpl.getInstance();
	
	private SysMessageNotifyServiceImpl()
	{}
	
	public static SysMessageNotifyServiceImpl getInstance()
	{
		return SERVICE;
	}

	@Override
	public void pushNotify(SysMessageNotify model) throws Exception
	{
		///获取系统消息通知最大ID
		Long notifyId = sysMessageNotifyRedis.getMaxNotifyId();
		JSONObject json = model.toJson();
		if(null == json)
			return;
		
		json.put("notify_id", notifyId);
		json.put("write_data_type", WriteDataType.SYS_MESSAGE_NOTIFY);
		writeQueue.put(json.toString());
	}

	@Override
	public SysMessageNotify getInfo(long notifyId) throws Exception
	{
		return sysMessageNotifyRedis.getInfo(notifyId);
	}

	@Override
	public List<SysMessageNotify> getList(long userId) throws Exception
	{
		Set<String> notifyIds = sysMessageNotifyRedis.getList(userId);
		if(null == notifyIds || notifyIds.size() == 0)
			return null;
		
		Iterator<String> iterator = notifyIds.iterator();
		Long notifyId;
		List<SysMessageNotify> list = new ArrayList<SysMessageNotify>();
		while(iterator.hasNext())
		{
			notifyId = Long.parseLong(iterator.next());
			if(null == notifyId || notifyId == 0)
				continue;
			
			SysMessageNotify notify = getInfo(notifyId);
			if(null == notify)
				continue;
			
			list.add(notify);
		}
		
		///清空用户的系统消息通知列表
		sysMessageNotifyRedis.deleteByUser(userId);
		///更新用户的系统消息通知状态
		sysMessageNotifyDao.updateStatusByUserId(userId, SysMessageNotifyStatus.READ);
		return list;
	}

	@Override
	public List<SysMessageNotify> getList(long userId, long start, long size) throws Exception
	{
		return sysMessageNotifyDao.getList(userId, start, size);
	}
}