package com.mofang.chat.business.model;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.chat.business.sysconf.common.SysMessageNotifyStatus;
import com.mofang.framework.data.mysql.core.annotation.ColumnName;
import com.mofang.framework.data.mysql.core.annotation.PrimaryKey;
import com.mofang.framework.data.mysql.core.annotation.TableName;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
@TableName(name="sys_message_notify")
public class SysMessageNotify
{
	@PrimaryKey
	@ColumnName(name="notify_id")
	private Long notifyId;
	@ColumnName(name="user_id")
	private Long userId;
	@ColumnName(name="message_type")
	private Integer messageType;
	@ColumnName(name="message_category")
	private String messageCategory;
	@ColumnName(name="title")
	private String title;
	@ColumnName(name="detail")
	private String detail;
	@ColumnName(name="source")
	private String source;
	@ColumnName(name="icon")
	private String icon;
	@ColumnName(name="is_show_notify")
	private Boolean isShowNotify;
	@ColumnName(name="click_action")
	private String clickAction;
	@ColumnName(name="create_time")
	private Date createTime;
	@ColumnName(name="status")
	private Integer status = SysMessageNotifyStatus.UNREAD;
	
	public Long getNotifyId()
	{
		return notifyId;
	}
	
	public void setNotifyId(Long notifyId)
	{
		this.notifyId = notifyId;
	}
	
	public Long getUserId()
	{
		return userId;
	}
	
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	
	public Integer getMessageType()
	{
		return messageType;
	}
	
	public void setMessageType(Integer messageType)
	{
		this.messageType = messageType;
	}
	
	public String getMessageCategory()
	{
		return messageCategory;
	}
	
	public void setMessageCategory(String messageCategory)
	{
		this.messageCategory = messageCategory;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public String getDetail()
	{
		return detail;
	}
	
	public void setDetail(String detail)
	{
		this.detail = detail;
	}
	
	public String getSource() 
	{
		return source;
	}
	
	public void setSource(String source)
	{
		this.source = source;
	}
	
	public String getIcon() 
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
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
	
	public Date getCreateTime()
	{
		return createTime;
	}
	
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	
	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("notify_id", notifyId);
			json.put("user_id", userId);
			json.put("msg_type", messageType);
			json.put("msg_category", messageCategory);
			json.put("title", title);
			json.put("detail", detail);
			JSONObject sourceJson = new JSONObject();
			if(!StringUtil.isNullOrEmpty(source))
				sourceJson = new JSONObject(source);
			json.put("source", sourceJson);
			json.put("icon", (icon == null ? "" : icon));
			json.put("is_show_notify", isShowNotify);
			json.put("click_act", clickAction);
			json.put("create_time", createTime);
			json.put("status", status);
			return json;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static SysMessageNotify buildByJson(JSONObject json)
	{
		SysMessageNotify model = new SysMessageNotify();
		try
		{
			model.setNotifyId(json.optLong("notify_id", 0L));
			model.setUserId(json.optLong("user_id", 0L));
			model.setMessageType(json.optInt("msg_type", 1));
			model.setMessageCategory(json.optString("msg_category", ""));
			model.setTitle(json.optString("title", ""));
			model.setDetail(json.optString("detail", ""));
			JSONObject srcJson = json.optJSONObject("source");
			if(null == srcJson)
				srcJson = new JSONObject();
			model.setSource(srcJson.toString());
			model.setIcon(json.optString("icon", ""));
			model.setIsShowNotify(json.optBoolean("is_show_notify", false));
			model.setClickAction(json.optString("click_act", ""));
			model.setCreateTime(new Date(json.optLong("create_time", System.currentTimeMillis())));
			model.setStatus(json.optInt("status", SysMessageNotifyStatus.UNREAD));
			return model;
		}
		catch(Exception e)
		{
			return null;
		}
	}
}