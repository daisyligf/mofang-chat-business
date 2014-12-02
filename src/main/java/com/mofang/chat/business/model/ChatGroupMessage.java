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
@TableName(name="chat_group_message")
public class ChatGroupMessage
{
	@PrimaryKey
	@ColumnName(name="message_id")
	private Long messageId;
	@ColumnName(name="group_id")
	private Long groupId;
	@ColumnName(name="user_id")
	private Long userId;
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
	@ColumnName(name="status")
	private Integer status;
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
	
	public Long getGroupId()
	{
		return groupId;
	}
	
	public void setGroupId(Long groupId)
	{
		this.groupId = groupId;
	}
	
	public Long getUserId()
	{
		return userId;
	}
	
	public void setUserId(Long userId)
	{
		this.userId = userId;
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
	
	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status) 
	{
		this.status = status;
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
			json.put("group_id", groupId);
			json.put("uid", userId);
			json.put("content", content);
			json.put("msg_type", messageType);
			json.put("duration", duration);
			json.put("is_show_notify", isShowNotify);
			json.put("click_action", clickAction);
			json.put("status", status);
			json.put("create_time", createTime.getTime());
			return json;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public static ChatGroupMessage toMysqlModel(JSONObject json)
	{
		ChatGroupMessage model = new ChatGroupMessage();
		model.setMessageId(json.optLong("msg_id", 0L));
		model.setGroupId(json.optLong("group_id", 0L));
		model.setUserId(json.optLong("uid", 0L));
		model.setMessageType(json.optInt("msg_type"));
		model.setDuration(json.optInt("duration"));
		model.setIsShowNotify(json.optBoolean("is_show_notify", false));
		model.setStatus(json.optInt("status"));
		model.setCreateTime(new Date(json.optLong("create_time")));
		model.setContent( json.optString("content", ""));
		model.setClickAction(json.optString("click_action", ""));
		return model;
	}
}