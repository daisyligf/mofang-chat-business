package com.mofang.chat.business.entity;

import java.util.List;

/**
 * 
 * @author zhaodx
 *
 */
public class PrivateMessageCollection
{
	private int count;
	private List<PrivateMessage> messages;
	
	public int getCount() 
	{
		return count;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public List<PrivateMessage> getMessage() 
	{
		return messages;
	}
	
	public void setMessage(List<PrivateMessage> messages) 
	{
		this.messages = messages;
	}
}