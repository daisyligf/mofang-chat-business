package com.mofang.chat.business.entity;

/**
 * 
 * @author zhaodx
 *
 */
public class BaseMessage implements Comparable<BaseMessage>
{
	protected Long messageId;
	protected Integer messageType;
	protected String content;
	protected Integer chatType;
	protected Integer duration;
	protected Long timeStamp;
	protected Boolean isShowNotify;
	protected String clickAction;
	protected Long fromUserId;
	protected Integer status;
	
	public Long getMessageId() 
	{
		return messageId;
	}

	public void setMessageId(Long messageId)
	{
		this.messageId = messageId;
	}

	public Integer getMessageType()
	{
		return messageType;
	}

	public void setMessageType(Integer messageType)
	{
		this.messageType = messageType;
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}

	public Integer getChatType() 
	{
		return chatType;
	}

	public void setChatType(Integer chatType)
	{
		this.chatType = chatType;
	}

	public Integer getDuration()
	{
		return duration;
	}

	public void setDuration(Integer duration)
	{
		this.duration = duration;
	}

	public Long getTimeStamp()
	{
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) 
	{
		this.timeStamp = timeStamp;
	}

	public Boolean isShowNotify() 
	{
		return isShowNotify;
	}

	public void setShowNotify(Boolean isShowNotify)
	{
		this.isShowNotify = isShowNotify;
	}

	public String getClickAction()
	{
		return clickAction;
	}

	public void setClickAction(String clickAction)
	{
		this.clickAction = clickAction;
	}

	public Long getFromUserId()
	{
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId)
	{
		this.fromUserId = fromUserId;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	@Override
	public int compareTo(BaseMessage o)
	{
		return (int)(this.getTimeStamp() - o.getTimeStamp());
	}
}