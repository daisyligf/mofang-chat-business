package com.mofang.chat.business.entity;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.chat.business.model.ChatPrivateMessage;
import com.mofang.chat.business.sysconf.common.ChatType;

/**
 * 
 * @author zhaodx
 *
 */
public class PrivateMessage extends BaseMessage
{
	private Long toUserId;
	protected Long expireTime;
	private Integer unreadCount;
	
	public Long getToUserId()
	{
		return toUserId;
	}
	
	public void setToUserId(Long toUserId) 
	{
		this.toUserId = toUserId;
	}

	public Long getExpireTime() 
	{
		return expireTime;
	}

	public void setExpireTime(Long expireTime)
	{
		this.expireTime = expireTime;
	}

	public Integer getUnreadCount()
	{
		return unreadCount;
	}
	
	public void setUnreadCount(Integer unreadCount)
	{
		this.unreadCount = unreadCount;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("to_uid", toUserId);
			json.put("unread_count", unreadCount);
			json.put("content", content == null ? "" : content);
			json.put("msg_type", messageType);
			json.put("duration", duration);
			json.put("chat_type", chatType);
			json.put("time_stamp", System.currentTimeMillis());
			json.put("is_show_notify", isShowNotify);
			json.put("click_act", clickAction);
			json.put("expire", expireTime);
			json.put("from_uid", fromUserId);
			return json;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public ChatPrivateMessage toMysqlModel()
	{
		ChatPrivateMessage model = new ChatPrivateMessage();
		model.setMessageId(messageId);
		model.setUserId(fromUserId);
		model.setToUserId(toUserId);
		model.setContent(content);
		model.setOriginalContent(content);
		model.setMessageType(messageType);
		model.setDuration(duration);
		model.setIsShowNotify(isShowNotify);
		model.setClickAction(clickAction);
		model.setExpireTime(new Date(expireTime));
		model.setCreateTime(new Date(timeStamp));
		return model;
	}
	
	public static PrivateMessage buildByJson(JSONObject json)
	{
		PrivateMessage model = new PrivateMessage();
		try
		{
			model.setToUserId(json.optLong("to_uid", 0L));
			model.setUnreadCount(json.optInt("unread_count", 0));
			model.setMessageId(json.optLong("msg_id", 0L));
			model.setContent(json.optString("content", ""));
			model.setMessageType(json.optInt("msg_type", 1));
			model.setDuration(json.optInt("duration", 0));
			model.setChatType(json.optInt("chat_type", 1));
			model.setTimeStamp(json.optLong("time_stamp", System.currentTimeMillis()));
			model.setShowNotify(json.optBoolean("is_show_notify", false));
			model.setClickAction(json.optString("click_act", ""));
			model.setExpireTime(json.optLong("expire", 0L));
			model.setFromUserId(json.optLong("from_uid", 0));
			return model;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static PrivateMessage buildByMysqlModel(ChatPrivateMessage model)
	{
		PrivateMessage entity = new PrivateMessage();
		entity.setMessageId(model.getMessageId());
		entity.setFromUserId(model.getUserId());
		entity.setToUserId(model.getToUserId());
		entity.setContent(model.getContent());
		entity.setMessageType(model.getMessageType());
		entity.setDuration(model.getDuration());
		entity.setChatType(ChatType.PRIVATE);
		entity.setShowNotify(model.getIsShowNotify());
		entity.setClickAction(model.getClickAction());
		entity.setExpireTime(model.getExpireTime().getTime());
		entity.setTimeStamp(model.getCreateTime().getTime());
		return entity;
	}
}