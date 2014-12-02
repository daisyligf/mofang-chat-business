package com.mofang.chat.business.entity;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.chat.business.model.ChatRoomMessage;
import com.mofang.chat.business.sysconf.common.ChatType;

/**
 * 
 * @author zhaodx
 *
 */
public class RoomMessage extends BaseMessage
{
	private Integer roomId;
	private String fontColor;

	public Integer getRoomId() 
	{
		return roomId;
	}

	public void setRoomId(Integer roomId) 
	{
		this.roomId = roomId;
	}
	
	public String getFontColor()
	{
		return fontColor;
	}

	public void setFontColor(String fontColor)
	{
		this.fontColor = fontColor;
	}

	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("rid", roomId);
			json.put("content", content == null ? "" : content);
			json.put("msg_type", messageType);
			json.put("duration", duration);
			json.put("font_color", fontColor == null ? "" : fontColor);
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
	
	public ChatRoomMessage toMysqlModel()
	{
		ChatRoomMessage model = new ChatRoomMessage();
		model.setMessageId(messageId);
		model.setRoomId(roomId);
		model.setUserId(fromUserId);
		model.setContent(content);
		model.setOriginalContent(content);
		model.setMessageType(messageType);
		model.setDuration(duration);
		model.setFontColor(fontColor);
		model.setIsShowNotify(isShowNotify);
		model.setClickAction(clickAction);
		model.setStatus(1);
		model.setCreateTime(new Date(timeStamp));
		return model;
	}
	
	public static RoomMessage buildByJson(JSONObject json)
	{
		RoomMessage model = new RoomMessage();
		try
		{
			model.setRoomId(json.optInt("rid", 0));
			model.setMessageId(json.optLong("msg_id", 0L));
			model.setContent(json.optString("content", ""));
			model.setMessageType(json.optInt("msg_type", 1));
			model.setDuration(json.optInt("duration", 0));
			model.setFontColor(json.optString("font_color", ""));
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
	
	public static RoomMessage buildByMysqlModel(ChatRoomMessage model)
	{
		RoomMessage entity = new RoomMessage();
		entity.setRoomId(model.getRoomId());
		entity.setMessageId(model.getMessageId());
		entity.setFromUserId(model.getUserId());
		entity.setContent(model.getContent());
		entity.setMessageType(model.getMessageType());
		entity.setDuration(model.getDuration());
		entity.setFontColor(model.getFontColor());
		entity.setChatType(ChatType.ROOM);
		entity.setShowNotify(model.getIsShowNotify());
		entity.setClickAction(model.getClickAction());
		entity.setTimeStamp(model.getCreateTime().getTime());
		return entity;
	}
}