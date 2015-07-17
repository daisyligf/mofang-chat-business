package com.mofang.chat.business.model;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.chat.business.sysconf.common.FeedRecommendType;
import com.mofang.chat.business.sysconf.common.FeedRecommendNotifyStatus;
import com.mofang.framework.data.mysql.core.annotation.ColumnName;
import com.mofang.framework.data.mysql.core.annotation.TableName;

/**
 * 
 * @author zhaodx
 *
 */
@TableName(name = "feed_recommend_notify")
public class FeedRecommendNotify
{
	@ColumnName(name = "notify_id")
	private Long notifyId;
	@ColumnName(name = "user_id")
	private Long userId;
	@ColumnName(name = "thread_id")
	private Long threadId;
	@ColumnName(name = "subject")
	private String subject;
	@ColumnName(name = "recommend_type")
	private Integer recommendType = FeedRecommendType.THREAD;
	@ColumnName(name = "recommend_user_id")
	private Long recommendUserId;
	@ColumnName(name = "post_id")
	private Long postId;
	@ColumnName(name = "forum_id")
	private Long forumId;
	@ColumnName(name = "forum_name")
	private String forumName;
	@ColumnName(name = "position")
	private Integer position;
	@ColumnName(name = "status")
	private int status = FeedRecommendNotifyStatus.UNREAD;
	@ColumnName(name = "is_show_notify")
	private Boolean isShowNotify;
	@ColumnName(name = "click_action")
	private String clickAction;
	@ColumnName(name = "create_time")
	private Date createTime;

	public Long getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(Long notifyId) {
		this.notifyId = notifyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getThreadId() {
		return threadId;
	}

	public void setThreadId(Long threadId) {
		this.threadId = threadId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getRecommendType() {
		return recommendType;
	}

	public void setRecommendType(Integer recommendType) {
		this.recommendType = recommendType;
	}

	public Long getRecommendUserId() {
		return recommendUserId;
	}

	public void setRecommendUserId(Long recommendUserId) {
		this.recommendUserId = recommendUserId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public String getForumName() {
		return forumName;
	}

	public void setForumName(String forumName) {
		this.forumName = forumName;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Boolean getIsShowNotify() {
		return isShowNotify;
	}

	public void setIsShowNotify(Boolean isShowNotify) {
		this.isShowNotify = isShowNotify;
	}

	public String getClickAction() {
		return clickAction;
	}

	public void setClickAction(String clickAction) {
		this.clickAction = clickAction;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("notify_id", notifyId);
			json.put("user_id", userId);
			json.put("thread_id", threadId);
			json.put("subject", subject);
			json.put("recommend_type", recommendType);
			json.put("recommend_user_id", recommendUserId);
			json.put("post_id", postId);
			json.put("forum_id", forumId);
			json.put("forum_name", forumName);
			json.put("position", position);
			json.put("status", status);
			json.put("is_show_notify", isShowNotify);
			json.put("click_act", clickAction);
			json.put("create_time", createTime);
			return json;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static FeedRecommendNotify buildByJson(JSONObject json)
	{
		FeedRecommendNotify model = new FeedRecommendNotify();
		try
		{
			model.setNotifyId(json.optLong("notify_id", 0L));
			model.setUserId(json.optLong("user_id", 0L));
			model.setThreadId(json.optLong("thread_id", 0L));
			model.setSubject(json.optString("subject", ""));
			model.setRecommendType(json.optInt("recommend_type", FeedRecommendType.THREAD));
			model.setRecommendUserId(json.optLong("recommend_user_id", 0L));
			model.setPostId(json.optLong("post_id", 0L));
			model.setForumId(json.optLong("forum_id", 0L));
			model.setForumName(json.optString("forum_name", ""));
			model.setPosition(json.optInt("position", 0));
			model.setStatus(json.optInt("status", FeedRecommendNotifyStatus.UNREAD));
			model.setIsShowNotify(json.optBoolean("is_show_notify", false));
			model.setClickAction(json.optString("click_act", ""));
			model.setCreateTime(new Date(json.optLong("create_time", System.currentTimeMillis())));
			return model;
		}
		catch(Exception e)
		{
			return null;
		}
	}
}