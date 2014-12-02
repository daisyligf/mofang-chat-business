package com.mofang.chat.business.entity;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.chat.business.model.ChatGroupMessage;
import com.mofang.chat.business.sysconf.common.ChatType;

/**
 * 
 * @author zhaodx
 *
 */
public class GroupMessage extends BaseMessage
{
	private Long groupId;
	private Integer unreadCount;
	
	public Long getGroupId()
	{
		return groupId;
	}
	
	public void setGroupId(Long groupId)
	{
		this.groupId = groupId;
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
			json.put("group_id", groupId);
			json.put("unread_count", unreadCount);
			json.put("content", content == null ? "" : content);
			json.put("msg_type", messageType);
			json.put("duration", duration);
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
	
	public ChatGroupMessage toMysqlModel()
	{
		ChatGroupMessage model = new ChatGroupMessage();
		model.setMessageId(messageId);
		model.setGroupId(groupId);
		model.setUserId(fromUserId);
		model.setContent(content);
		model.setOriginalContent(content);
		model.setMessageType(messageType);
		model.setDuration(duration);
		model.setIsShowNotify(isShowNotify);
		model.setClickAction(clickAction);
		model.setStatus(1);
		model.setCreateTime(new Date(timeStamp));
		return model;
	}
	
	public static GroupMessage buildByJson(JSONObject json)
	{
		GroupMessage model = new GroupMessage();
		try
		{
			model.setGroupId(json.optLong("group_id", 0L));
			model.setUnreadCount(json.optInt("unread_count", 0));
			model.setMessageId(json.optLong("msg_id", 0L));
			model.setContent(json.optString("content", ""));
			model.setMessageType(json.optInt("msg_type", 1));
			model.setDuration(json.optInt("duration", 0));
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
	
	public static GroupMessage buildByMysqlModel(ChatGroupMessage model)
	{
		GroupMessage entity = new GroupMessage();
		entity.setMessageId(model.getMessageId());
		entity.setFromUserId(model.getUserId());
		entity.setGroupId(model.getGroupId());
		entity.setContent(model.getContent());
		entity.setMessageType(model.getMessageType());
		entity.setDuration(model.getDuration());
		entity.setChatType(ChatType.GROUP);
		entity.setShowNotify(model.getIsShowNotify());
		entity.setClickAction(model.getClickAction());
		entity.setTimeStamp(model.getCreateTime().getTime());
		return entity;
	}
}