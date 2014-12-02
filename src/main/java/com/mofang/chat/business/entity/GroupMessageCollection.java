package com.mofang.chat.business.entity;

import java.util.List;

/**
 * 
 * @author zhaodx
 *
 */
public class GroupMessageCollection
{
	private int count;
	private List<GroupMessage> messages;
	
	public int getCount() 
	{
		return count;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public List<GroupMessage> getMessage() 
	{
		return messages;
	}
	
	public void setMessage(List<GroupMessage> messages) 
	{
		this.messages = messages;
	}
}