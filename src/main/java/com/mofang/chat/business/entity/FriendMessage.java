package com.mofang.chat.business.entity;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.chat.business.model.ChatFriendMessage;
import com.mofang.chat.business.sysconf.common.ChatType;

/**
 * 
 * @author zhaodx
 *
 */
public class FriendMessage extends BaseMessage
{
	private Long toUserId;
	
	public Long getToUserId()
	{
		return toUserId;
	}
	
	public void setToUserId(Long toUserId) 
	{
		this.toUserId = toUserId;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("to_uid", toUserId);
			json.put("content", content == null ? "" : content);
			json.put("msg_type", messageType);
			json.put("chat_type", chatType);
			json.put("time_stamp", System.currentTimeMillis());
			json.put("is_show_notify", isShowNotify);
			json.put("click_act", clickAction);
			json.put("from_uid", fromUserId);
			return json;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public ChatFriendMessage toMysqlModel()
	{
		ChatFriendMessage model = new ChatFriendMessage();
		model.setMessageId(messageId);
		model.setUserId(fromUserId);
		model.setToUserId(toUserId);
		model.setContent(content);
		model.setMessageType(messageType);
		model.setIsShowNotify(isShowNotify);
		model.setClickAction(clickAction);
		model.setCreateTime(new Date(timeStamp));
		return model;
	}
	
	public static FriendMessage buildByJson(JSONObject json)
	{
		FriendMessage model = new FriendMessage();
		try
		{
			model.setToUserId(json.optLong("to_uid", 0L));
			model.setMessageId(json.optLong("msg_id", 0L));
			model.setContent(json.optString("content", ""));
			model.setMessageType(json.optInt("msg_type", 1));
			model.setChatType(json.optInt("chat_type", 1));
			model.setTimeStamp(json.optLong("time_stamp", System.currentTimeMillis()));
			model.setShowNotify(json.optBoolean("is_show_notify", false));
			model.setClickAction(json.optString("click_act", ""));
			model.setFromUserId(json.optLong("from_uid", 0));
			return model;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static FriendMessage buildByMysqlModel(ChatFriendMessage model)
	{
		FriendMessage entity = new FriendMessage();
		entity.setMessageId(model.getMessageId());
		entity.setFromUserId(model.getUserId());
		entity.setToUserId(model.getToUserId());
		entity.setContent(model.getContent());
		entity.setMessageType(model.getMessageType());
		entity.setChatType(ChatType.PRIVATE);
		entity.setShowNotify(model.getIsShowNotify());
		entity.setClickAction(model.getClickAction());
		entity.setTimeStamp(model.getCreateTime().getTime());
		return entity;
	}
}