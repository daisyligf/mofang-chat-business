package com.mofang.chat.business.model;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.chat.business.sysconf.common.PostReplyNotifyStatus;
import com.mofang.framework.data.mysql.core.annotation.ColumnName;
import com.mofang.framework.data.mysql.core.annotation.PrimaryKey;
import com.mofang.framework.data.mysql.core.annotation.TableName;

/**
 * 
 * @author zhaodx
 *
 */
@TableName(name="post_reply_notify")
public class PostReplyNotify
{
	@PrimaryKey
	@ColumnName(name="notify_id")
	private Long notifyId;
	@ColumnName(name="user_id")
	private Long userId;
	@ColumnName(name="message_type")
	private Integer messageType;
	@ColumnName(name="post_id")
	private Integer postId;
	@ColumnName(name="post_title")
	private String postTitle;
	@ColumnName(name="reply_id")
	private Integer replyId;
	@ColumnName(name="reply_text")
	private String replyText;
	@ColumnName(name="reply_pictures")
	private String replyPictures;
	@ColumnName(name="reply_time")
	private Date replyTime;
	@ColumnName(name="reply_user_id")
	private Long replyUserId;
	@ColumnName(name="reply_type")
	private Integer replyType;
	@ColumnName(name="is_show_notify")
	private Boolean isShowNotify;
	@ColumnName(name="click_action")
	private String clickAction;
	@ColumnName(name="create_time")
	private Date createTime;
	@ColumnName(name="status")
	private Integer status = PostReplyNotifyStatus.UNREAD;
	
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
	
	public Integer getPostId()
	{
		return postId;
	}
	
	public void setPostId(Integer postId)
	{
		this.postId = postId;
	}
	
	public String getPostTitle()
	{
		return postTitle;
	}
	
	public void setPostTitle(String postTitle) 
	{
		this.postTitle = postTitle;
	}
	
	public Integer getReplyId()
	{
		return replyId;
	}
	
	public void setReplyId(Integer replyId) 
	{
		this.replyId = replyId;
	}
	
	public String getReplyText() 
	{
		return replyText;
	}
	
	public void setReplyText(String replyText) 
	{
		this.replyText = replyText;
	}
	
	public String getReplyPictures()
	{
		return replyPictures;
	}
	
	public void setReplyPictures(String replyPictures)
	{
		this.replyPictures = replyPictures;
	}
	
	public Date getReplyTime()
	{
		return replyTime;
	}
	
	public void setReplyTime(Date replyTime)
	{
		this.replyTime = replyTime;
	}
	
	public Long getReplyUserId() 
	{
		return replyUserId;
	}
	
	public void setReplyUserId(Long replyUserId)
	{
		this.replyUserId = replyUserId;
	}
	
	public Integer getReplyType()
	{
		return replyType;
	}
	
	public void setReplyType(Integer replyType)
	{
		this.replyType = replyType;
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
			json.put("post_id", postId);
			json.put("post_title", postTitle);
			json.put("reply_id", replyId);
			json.put("reply_text", replyText);
			json.put("reply_pictures", replyPictures);
			json.put("reply_time", replyTime);
			json.put("reply_user_id", replyUserId);
			json.put("reply_type", replyType);
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
	
	public static PostReplyNotify buildByJson(JSONObject json)
	{
		PostReplyNotify model = new PostReplyNotify();
		try
		{
			model.setNotifyId(json.optLong("notify_id", 0L));
			model.setUserId(json.optLong("user_id", 0L));
			model.setMessageType(json.optInt("msg_type", 1));
			model.setPostId(json.optInt("post_id", 0));
			model.setPostTitle(json.optString("post_title", ""));
			model.setReplyId(json.optInt("reply_id", 0));
			model.setReplyText(json.optString("reply_text", ""));
			model.setReplyPictures(json.optString("reply_pictures", ""));
			model.setReplyTime(new Date(json.optLong("reply_time", System.currentTimeMillis())));
			model.setReplyUserId(json.optLong("reply_user_id", 0L));
			model.setReplyType(json.optInt("reply_type", 1));
			model.setIsShowNotify(json.optBoolean("is_show_notify", false));
			model.setClickAction(json.optString("click_act", ""));
			model.setCreateTime(new Date(json.optLong("create_time", System.currentTimeMillis())));
			model.setStatus(json.optInt("status", PostReplyNotifyStatus.UNREAD));
			return model;
		}
		catch(Exception e)
		{
			return null;
		}
	}
}