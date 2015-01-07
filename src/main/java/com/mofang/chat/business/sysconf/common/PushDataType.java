package com.mofang.chat.business.sysconf.common;

/**
 * 
 * @author zhaodx
 *
 */
public class PushDataType
{
	/**
	 * 公聊通知
	 */
	public final static int ROOM_NOTIFY = 1;
	
	/**
	 * 私聊通知
	 */
	public final static int PRIVATE_NOTIFY = 2;
	
	/**
	 * 好友申请/处理结果通知
	 */
	public final static int FRIEND_NOTIFY = 3;
	
	/**
	 * 公聊活动通知(如抢答等)
	 */
	public final static int ROOM_ACTIVITY_NOTIFY = 4;
	
	/**
	 * 群组通知
	 */
	public final static int GROUP_NOTIFY = 5;
	
	/**
	 * 帖子回复通知
	 */
	public final static int POST_REPLY_NOTIFY = 6;
	
	/**
	 * 系统消息通知
	 */
	public final static int SYS_MESSAGE_NOTIFY = 7;
	
	/**
	 * 用户任务通知
	 */
	public final static int USER_TASK_NOTIFY = 8;
	
	/**
	 * 用户获得勋章通知
	 */
	public final static int USER_MEDAL_NOTIFY = 9;
}