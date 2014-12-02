package com.mofang.chat.business.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.mofang.chat.business.entity.GroupMessage;
import com.mofang.chat.business.entity.GroupMessageCollection;
import com.mofang.chat.business.model.ChatGroupMessage;
import com.mofang.chat.business.mysql.ChatGroupMessageDao;
import com.mofang.chat.business.mysql.impl.ChatGroupMessageDaoImpl;
import com.mofang.chat.business.redis.GroupMessageRedis;
import com.mofang.chat.business.redis.WriteQueueRedis;
import com.mofang.chat.business.redis.impl.GroupMessageRedisImpl;
import com.mofang.chat.business.redis.impl.WriteQueueRedisImpl;
import com.mofang.chat.business.service.GroupMessageService;
import com.mofang.chat.business.sysconf.common.WriteDataType;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class GroupMessageServiceImpl implements GroupMessageService
{
	private final static GroupMessageServiceImpl SERVICE = new GroupMessageServiceImpl();
	private GroupMessageRedis groupMessageRedis = GroupMessageRedisImpl.getInstance();
	private ChatGroupMessageDao groupMessageDao = ChatGroupMessageDaoImpl.getInstance();
	private WriteQueueRedis writeQueue = WriteQueueRedisImpl.getInstance();
	
	private GroupMessageServiceImpl()
	{}
	
	public static GroupMessageServiceImpl getInstance()
	{
		return SERVICE;
	}

	@Override
	public long sendMessage(GroupMessage model) throws Exception
	{
		///获取群组消息最大ID
		Long messageId = groupMessageRedis.getMaxMessageId();
		JSONObject json = model.toJson();
		if(null == json)
			return 0L;
		
		json.put("msg_id", messageId);
		json.put("write_data_type", WriteDataType.GROUP_MESSAGE);
		writeQueue.put(json.toString());
		return messageId;
	}

	@Override
	public GroupMessage getNotifyInfo(long userId, long groupId) throws Exception
	{
		return groupMessageRedis.getNotifyInfo(userId, groupId);
	}

	@Override
	public List<GroupMessage> getNotifyList(long userId) throws Exception
	{
		Map<Long, GroupMessage> notifyMap = groupMessageRedis.getNotifyList(userId);
		if(null == notifyMap || notifyMap.size() == 0)
			return null;
		
		Iterator<Long> iterator = notifyMap.keySet().iterator();
		Long groupId;
		List<GroupMessage> list = new ArrayList<GroupMessage>();
		while(iterator.hasNext())
		{
			groupId = iterator.next();
			if(null == groupId || groupId == 0)
				continue;
			
			GroupMessage message = notifyMap.get(groupId);
			if(null == message)
				continue;
			
			list.add(message);
		}
		///按时间倒序
		Collections.sort(list,  Collections.reverseOrder());
		return list;
	}

	@Override
	public GroupMessageCollection getMessages(long userId, long groupId, long minMessageId, long maxMessageId, int pageSize) throws Exception
	{
		try
		{
			///清空redis中该用户的当前群组的未读数
			groupMessageRedis.clearNotifyUnreadCount(userId, groupId);
			
			///获取群组的聊天信息
			Set<String> messageSet = groupMessageRedis.getList(groupId, minMessageId, maxMessageId, pageSize);
			List<ChatGroupMessage> messages = null;
			int recordCount = 0;
			if(null != messageSet && messageSet.size() > 0)
			{
				recordCount = messageSet.size();
				if(recordCount < pageSize)
				{
					///获取第一条记录，用于以此记录为界限，从数据库中获取余下的记录集合
					String[] ids = new String[recordCount];
					messageSet.toArray(ids);
					String lastId = ids[recordCount - 1];
					if(StringUtil.isLong(lastId))
					{
						///从数据库中获取
						messages = groupMessageDao.getMessages(groupId, minMessageId, Long.parseLong(lastId), (pageSize - recordCount));
						if(null != messages && messages.size() > 0)
						{
							recordCount += messages.size();
							for(int i=messages.size() - 1; i>=0; i--)
								messageSet.add(messages.get(i).getMessageId().toString());
						}
					}	
				}
			}
			else
			{
				///从数据库中获取
				messages = groupMessageDao.getMessages(groupId, minMessageId, maxMessageId, pageSize);
				if(null == messages || messages.size() == 0)
					return null;
				
				recordCount = messages.size();
				messageSet = new HashSet<String>();
				for(int i=messages.size() - 1; i>=0; i--)
					messageSet.add(messages.get(i).getMessageId().toString());
			}
			
			GroupMessageCollection msgCollection = new GroupMessageCollection();
			String[] messageIds = new String[recordCount];
			messageSet.toArray(messageIds);
			msgCollection.setCount(recordCount);
			int start = recordCount - 1;
			List<GroupMessage> msgList = new ArrayList<GroupMessage>();
			GroupMessage message = null;
			long messageId;
			ChatGroupMessage model = null;
			for(int i= start; i>=0; i--)
			{
				message = new GroupMessage();
				messageId = Long.parseLong(messageIds[i]);
				///先从redis中获取消息信息，如果redis中没有，再从mysql中获取
				model = groupMessageRedis.getInfo(messageId);
				if(null == model)
				{
					model = groupMessageDao.getInfo(messageId);
					if(null == model)
						continue;
				}
				
				message.setMessageId(model.getMessageId());
				message.setContent(model.getContent());
				message.setMessageType(model.getMessageType());
				message.setTimeStamp(model.getCreateTime().getTime());
				message.setFromUserId(model.getUserId());
				message.setDuration(model.getDuration());
				msgList.add(message);
			}
			msgCollection.setMessage(msgList);
			return msgCollection;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	@Override
	public boolean updateStatus(long messageId, int status) throws Exception
	{
		groupMessageRedis.updateStatus(messageId, status);
		groupMessageDao.updateStatus(messageId, status);
		return true;
	}
}