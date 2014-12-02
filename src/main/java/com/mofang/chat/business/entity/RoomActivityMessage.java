package com.mofang.chat.business.entity;

import org.json.JSONObject;

/**
 * 
 * @author zhaodx
 *
 */
public class RoomActivityMessage extends BaseMessage
{
	private Integer roomId;

	public Integer getRoomId() 
	{
		return roomId;
	}

	public void setRoomId(Integer roomId) 
	{
		this.roomId = roomId;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("room_id", roomId);
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
	
	public static RoomActivityMessage toModel(JSONObject json)
	{
		RoomActivityMessage model = new RoomActivityMessage();
		try
		{
			model.setRoomId(json.optInt("room_id", 0));
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
}