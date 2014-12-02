package com.mofang.chat.business.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.mofang.chat.business.entity.PrivateMessage;
import com.mofang.chat.business.entity.PrivateMessageCollection;
import com.mofang.chat.business.model.ChatPrivateMessage;
import com.mofang.chat.business.mysql.ChatPrivateMessageDao;
import com.mofang.chat.business.mysql.impl.ChatPrivateMessageDaoImpl;
import com.mofang.chat.business.redis.PrivateMessageRedis;
import com.mofang.chat.business.redis.UserRedis;
import com.mofang.chat.business.redis.WriteQueueRedis;
import com.mofang.chat.business.redis.impl.PrivateMessageRedisImpl;
import com.mofang.chat.business.redis.impl.UserRedisImpl;
import com.mofang.chat.business.redis.impl.WriteQueueRedisImpl;
import com.mofang.chat.business.service.PrivateMessageService;
import com.mofang.chat.business.sysconf.common.WriteDataType;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class PrivateMessageServiceImpl implements PrivateMessageService
{
	private final static PrivateMessageServiceImpl SERVICE = new PrivateMessageServiceImpl();
	private PrivateMessageRedis privateMessageRedis = PrivateMessageRedisImpl.getInstance();
	private UserRedis userRedis = UserRedisImpl.getInstance();
	private ChatPrivateMessageDao privateMessageDao = ChatPrivateMessageDaoImpl.getInstance();
	private WriteQueueRedis writeQueue = WriteQueueRedisImpl.getInstance();
	
	private PrivateMessageServiceImpl()
	{}
	
	public static PrivateMessageServiceImpl getInstance()
	{
		return SERVICE;
	}

	@Override
	public long sendMessage(PrivateMessage model) throws Exception
	{
		///获取私聊消息最大ID
		Long messageId = privateMessageRedis.getMaxMessageId();
		JSONObject json = model.toJson();
		if(null == json)
			return 0L;
		
		json.put("msg_id", messageId);
		json.put("write_data_type", WriteDataType.PRIVATE_MESSAGE);
		writeQueue.put(json.toString());
		return messageId;
	}

	@Override
	public PrivateMessage getPushNotify(long fromUserId, long toUserId) throws Exception
	{
		return userRedis.getUnreadCount(fromUserId, toUserId);
	}

	@Override
	public List<PrivateMessage> getPullNotify(long userId) throws Exception
	{
		Map<Long, PrivateMessage> unreadMap = userRedis.getUnreadCounts(userId);
		if(null == unreadMap || unreadMap.size() == 0)
			return null;
		
		Iterator<Long> iterator = unreadMap.keySet().iterator();
		Long fromUserId;
		List<PrivateMessage> list = new ArrayList<PrivateMessage>();
		while(iterator.hasNext())
		{
			fromUserId = iterator.next();
			if(null == fromUserId || fromUserId == 0)
				continue;
			
			PrivateMessage message = unreadMap.get(fromUserId);
			if(null == message)
				continue;
			
			///判断该通知是否已过期
			boolean isExpire = System.currentTimeMillis() - message.getExpireTime() > 0; 
			if(isExpire)
			{
				///将该消息信息从redis中删除以及从私聊对应的消息ID列表中删除
				ChatPrivateMessage privateMessage = new ChatPrivateMessage();
				privateMessage.setMessageId(message.getMessageId());
				privateMessage.setUserId(fromUserId);
				privateMessage.setToUserId(userId);
				privateMessageRedis.delete(privateMessage);
				///将该消息从私聊通知列表中删除
				userRedis.removeUnreadCount(fromUserId, userId);
				continue;
			}
			list.add(message);
		}
		///按时间倒序
		Collections.sort(list,  Collections.reverseOrder());
		return list;
	}

	@Override
	public PrivateMessageCollection getPullMessages(long fromUserId, long toUserId, long minMessageId, long maxMessageId, int pageSize) throws Exception
	{
		try
		{
			///清空redis中该用户的当前好友的未读数
			userRedis.removeUnreadCount(fromUserId, toUserId);
			
			Set<String> messageSet = privateMessageRedis.getList(fromUserId, toUserId, minMessageId, maxMessageId, pageSize);
			List<ChatPrivateMessage> messages = null;
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
						messages = privateMessageDao.getMessages(fromUserId, toUserId, minMessageId, Long.parseLong(lastId), (pageSize - recordCount));
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
				messages = privateMessageDao.getMessages(fromUserId, toUserId, minMessageId, maxMessageId, pageSize);
				if(null == messages || messages.size() == 0)
					return null;
				
				recordCount = messages.size();
				messageSet = new HashSet<String>();
				for(int i=messages.size() - 1; i>=0; i--)
					messageSet.add(messages.get(i).getMessageId().toString());
			}
			
			PrivateMessageCollection msgCollection = new PrivateMessageCollection();
			String[] messageIds = new String[recordCount];
			messageSet.toArray(messageIds);
			msgCollection.setCount(recordCount);
			int start = recordCount - 1;
			List<PrivateMessage> msgList = new ArrayList<PrivateMessage>();
			PrivateMessage message = null;
			long messageId;
			ChatPrivateMessage model = null;
			for(int i= start; i>=0; i--)
			{
				message = new PrivateMessage();
				messageId = Long.parseLong(messageIds[i]);
				///先从redis中获取消息信息，如果redis中没有，再从mysql中获取
				model = privateMessageRedis.getInfo(messageId);
				if(null == model)
				{
					model = privateMessageDao.getInfo(messageId);
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
}