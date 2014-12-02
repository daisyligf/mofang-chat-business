package com.mofang.chat.business.model;

import java.util.Date;

import com.mofang.framework.data.mysql.core.annotation.AutoIncrement;
import com.mofang.framework.data.mysql.core.annotation.ColumnName;
import com.mofang.framework.data.mysql.core.annotation.PrimaryKey;
import com.mofang.framework.data.mysql.core.annotation.TableName;

/**
 * 
 * @author zhaodx
 *
 */
@TableName(name="chat_friend_message")
public class ChatFriendMessage
{
	@PrimaryKey
	@AutoIncrement
	@ColumnName(name="message_id")
	private Long messageId;
	@ColumnName(name="user_id")
	private Long userId;
	@ColumnName(name="to_user_id")
	private Long toUserId;
	@ColumnName(name="content")
	private String content;
	@ColumnName(name="message_type")
	private Integer messageType;
	@ColumnName(name="is_show_notify")
	private Boolean isShowNotify;
	@ColumnName(name="click_action")
	private String ClickAction;
	@ColumnName(name="create_time")
	private Date createTime;
	
	public Long getMessageId() 
	{
		return messageId;
	}
	
	public void setMessageId(Long messageId)
	{
		this.messageId = messageId;
	}
	
	public Long getUserId()
	{
		return userId;
	}
	
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	
	public Long getToUserId() 
	{
		return toUserId;
	}
	
	public void setToUserId(Long toUserId)
	{
		this.toUserId = toUserId;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content) 
	{
		this.content = content;
	}
	
	public Integer getMessageType()
	{
		return messageType;
	}
	
	public void setMessageType(Integer messageType)
	{
		this.messageType = messageType;
	}
	
	public Boolean getIsShowNotify()
	{
		return isShowNotify;
	}

	public void setIsShowNotify(Boolean isShowNotify)
	{
		this.isShowNotify = isShowNotify;
	}

	public String getClickAction()
	{
		return ClickAction;
	}

	public void setClickAction(String clickAction)
	{
		ClickAction = clickAction;
	}

	public Date getCreateTime()
	{
		return createTime;
	}
	
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
}