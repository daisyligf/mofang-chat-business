package com.mofang.chat.business.model;

import com.mofang.framework.data.mysql.core.annotation.ColumnName;
import com.mofang.framework.data.mysql.core.annotation.TableName;

/**
 * 
 * @author zhaodx
 *
 */
@TableName(name="room_user")
public class RoomUser
{
	@ColumnName(name="room_id")
	private Integer roomId;
	@ColumnName(name="user_id")
	private Long userId;
	@ColumnName(name="msg_count")
	private Integer msgCount;
	
	public Integer getRoomId()
	{
		return roomId;
	}
	
	public void setRoomId(Integer roomId)
	{
		this.roomId = roomId;
	}
	
	public Long getUserId() 
	{
		return userId;
	}
	
	public void setUserId(Long userId) 
	{
		this.userId = userId;
	}
	
	public Integer getMsgCount() 
	{
		return msgCount;
	}
	
	public void setMsgCount(Integer msgCount)
	{
		this.msgCount = msgCount;
	}
}