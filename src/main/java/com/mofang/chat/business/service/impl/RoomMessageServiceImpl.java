package com.mofang.chat.business.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.mofang.chat.business.entity.RoomMessage;
import com.mofang.chat.business.entity.RoomMessageCollection;
import com.mofang.chat.business.model.ChatRoomMessage;
import com.mofang.chat.business.mysql.ChatRoomMessageDao;
import com.mofang.chat.business.mysql.impl.ChatRoomMessageDaoImpl;
import com.mofang.chat.business.redis.RoomMessageRedis;
import com.mofang.chat.business.redis.RoomRedis;
import com.mofang.chat.business.redis.WriteQueueRedis;
import com.mofang.chat.business.redis.impl.RoomMessageRedisImpl;
import com.mofang.chat.business.redis.impl.RoomRedisImpl;
import com.mofang.chat.business.redis.impl.WriteQueueRedisImpl;
import com.mofang.chat.business.service.RoomMessageService;
import com.mofang.chat.business.sysconf.common.WriteDataType;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class RoomMessageServiceImpl implements RoomMessageService
{
	private final static RoomMessageServiceImpl SERVICE = new RoomMessageServiceImpl();
	private RoomMessageRedis roomMessageRedis = RoomMessageRedisImpl.getInstance();
	private RoomRedis roomRedis = RoomRedisImpl.getInstance();
	private ChatRoomMessageDao roomMessageDao = ChatRoomMessageDaoImpl.getInstance();
	private WriteQueueRedis writeQueue = WriteQueueRedisImpl.getInstance();
	
	private RoomMessageServiceImpl()
	{}
	
	public static RoomMessageServiceImpl getInstance()
	{
		return SERVICE;
	}

	@Override
	public long sendMessage(RoomMessage model) throws Exception
	{
		///获取聊天室消息最大ID
		Long messageId = roomMessageRedis.getMaxMessageId();
		//Long messageId = 0L;
		JSONObject json = model.toJson();
		if(null == json)
			return 0L;
		
		json.put("msg_id", messageId);
		json.put("write_data_type", WriteDataType.ROOM_MESSAGE);
		writeQueue.put(json.toString());
		return messageId;
	}

	@Override
	public RoomMessage getPushNotify(int roomId) throws Exception
	{
		return null;
	}

	@Override
	public List<RoomMessage> getPullNotify(List<Integer> roomIds) throws Exception
	{
		List<RoomMessage> messages = new ArrayList<RoomMessage>();
		RoomMessage message = null;
		for(Integer roomId : roomIds)
		{
			message = new RoomMessage();
			Long timestamp = roomRedis.getLastTimestamp(roomId);
			if(null == timestamp)
				timestamp = System.currentTimeMillis();
			message.setRoomId(roomId);
			message.setTimeStamp(timestamp);
			messages.add(message);
		}
		return messages;
	}

	@Override
	public RoomMessageCollection getPullMessages(int roomId, long minMessageId, long maxMessageId, int pageSize) throws Exception 
	{
		RoomMessageCollection msgCollection = new RoomMessageCollection();
		try
		{
			msgCollection.setEnterRoomUserCount(roomRedis.getEnterUserCount(roomId));
			Set<String> messageSet = roomMessageRedis.getList(roomId, minMessageId, maxMessageId, pageSize);
			List<ChatRoomMessage> messages = null;
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
						messages = roomMessageDao.getMessages(roomId, minMessageId, Long.parseLong(lastId), (pageSize - recordCount));
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
				messages = roomMessageDao.getMessages(roomId, minMessageId, maxMessageId, pageSize);
				if(null == messages || messages.size() == 0)
					return msgCollection;
				
				recordCount = messages.size();
				messageSet = new HashSet<String>();
				for(int i=messages.size() - 1; i>=0; i--)
					messageSet.add(messages.get(i).getMessageId().toString());
			}
			
			String[] messageIds = new String[recordCount];
			messageSet.toArray(messageIds);
			msgCollection.setCount(recordCount);
			
			int start = recordCount - 1;
			List<RoomMessage> msgList = new ArrayList<RoomMessage>();
			RoomMessage message = null;
			long messageId;
			ChatRoomMessage model = null;
			for(int i= start; i>=0; i--)
			{
				message = new RoomMessage();
				messageId = Long.parseLong(messageIds[i]);
				model = roomMessageRedis.getInfo(messageId);
				if(null == model)
				{
					model = roomMessageDao.getInfo(messageId);
					if(null == model)
						continue;
				}
				
				message.setMessageId(model.getMessageId());
				message.setContent(model.getContent());
				message.setMessageType(model.getMessageType());
				message.setTimeStamp(model.getCreateTime().getTime());
				message.setFromUserId(model.getUserId());
				message.setStatus(model.getStatus());
				message.setDuration(model.getDuration());
				message.setFontColor(model.getFontColor());
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
		roomMessageRedis.updateStatus(messageId, status);
		roomMessageDao.updateStatus(messageId, status);
		return true;
	}
}