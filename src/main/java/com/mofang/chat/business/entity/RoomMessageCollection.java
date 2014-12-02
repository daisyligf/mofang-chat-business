package com.mofang.chat.business.entity;

import java.util.List;

/**
 * 
 * @author zhaodx
 *
 */
public class RoomMessageCollection
{
	private int count;
	private int enterRoomUserCount;
	private List<RoomMessage> messages;
	
	public int getCount() 
	{
		return count;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public int getEnterRoomUserCount()
	{
		return enterRoomUserCount;
	}
	
	public void setEnterRoomUserCount(int enterRoomUserCount)
	{
		this.enterRoomUserCount = enterRoomUserCount;
	}
	
	public List<RoomMessage> getMessage() 
	{
		return messages;
	}
	
	public void setMessage(List<RoomMessage> messages) 
	{
		this.messages = messages;
	}
}