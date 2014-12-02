package com.mofang.chat.business.model;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.framework.data.mysql.core.annotation.ColumnName;
import com.mofang.framework.data.mysql.core.annotation.PrimaryKey;
import com.mofang.framework.data.mysql.core.annotation.TableName;

/**
 * 
 * @author zhaodx
 *
 */
@TableName(name="chat_private_message")
public class ChatPrivateMessage
{
	@PrimaryKey
	@ColumnName(name="message_id")
	private Long messageId;
	@ColumnName(name="user_id")
	private Long userId;
	@ColumnName(name="to_user_id")
	private Long toUserId;
	@ColumnName(name="content")
	private String content;
	@ColumnName(name="original_content")
	private String originalContent;
	@ColumnName(name="message_type")
	private Integer messageType;
	@ColumnName(name="duration")
	private Integer duration;
	@ColumnName(name="is_show_notify")
	private Boolean isShowNotify;
	@ColumnName(name="click_action")
	private String clickAction;
	@ColumnName(name="expire_time")
	private Date expireTime;
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

	public String getOriginalContent() 
	{
		return originalContent;
	}

	public void setOriginalContent(String originalContent) 
	{
		this.originalContent = originalContent;
	}

	public Integer getMessageType()
	{
		return messageType;
	}
	
	public void setMessageType(Integer messageType)
	{
		this.messageType = messageType;
	}
	
	public Integer getDuration()
	{
		return duration;
	}

	public void setDuration(Integer duration)
	{
		this.duration = duration;
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
		return clickAction;
	}

	public void setClickAction(String clickAction)
	{
		this.clickAction = clickAction;
	}

	public Date getExpireTime()
	{
		return expireTime;
	}

	public void setExpireTime(Date expireTime)
	{
		this.expireTime = expireTime;
	}

	public Date getCreateTime()
	{
		return createTime;
	}
	
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	
	public JSONObject toJson() throws Exception
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("msg_id", messageId);
			json.put("uid", userId);
			json.put("to_uid", toUserId);
			json.put("content", content);
			json.put("msg_type", messageType);
			json.put("duration", duration);
			json.put("is_show_notify", isShowNotify);
			json.put("click_action", clickAction);
			json.put("expire_time", expireTime.getTime());
			json.put("create_time", createTime.getTime());
			return json;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public static ChatPrivateMessage toMysqlModel(JSONObject json)
	{
		ChatPrivateMessage model  = new ChatPrivateMessage();
		model.setMessageId(json.optLong("msg_id"));
		model.setUserId(json.optLong("uid"));
		model.setToUserId(json.optLong("to_uid"));
		model.setMessageType(json.optInt("msg_type"));
		model.setDuration(json.optInt("duration"));
		model.setIsShowNotify(json.optBoolean("is_show_notify"));
		model.setExpireTime(new Date(json.optLong("expire_time")));
		model.setCreateTime(new Date(json.optLong("create_time")));
		model.setContent( json.optString("content"));
		model.setClickAction(json.optString("click_action"));
		return model;
	}
}