package com.mofang.chat.business.sysconf;

/**
 * 
 * @author zhaodx
 *
 */
public class SysRedisKey
{
	/**
	 * 公聊消息自增ID rediskey
	 */
	public final static String ROOM_MESSAGE_ID_INCREMENT_KEY = "room_message_id";
	
	/**
	 * 私聊消息自增ID rediskey
	 */
	public final static String PRIVATE_MESSAGE_ID_INCREMENT_KEY = "private_message_id";
	
	/**
	 * Write Queue中聊天消息任务key
	 */
	public final static String WRITE_QUEUE_CHAT_MESSAGE_KEY = "write_queue_chat_message";
	
	/**
	 * 公聊push queue rediskey
	 */
	public final static String ROOM_MESSAGE_PUSH_QUEUE_KEY = "room_message_push_queue";
	
	/**
	 * 私聊push queue rediskey
	 */
	public final static String PRIVATE_MESSAGE_PUSH_QUEUE_KEY = "private_message_push_queue";
	
	/**
	 * APNS推送消息队列
	 */
	public final static String APPLE_MESSAGE_PUSH_QUEUE_KEY = "apple_msg_notify";
	
	/**
	 * 为FE推送的通知队列key
	 */
	public final static String FRONTEND_PUSH_QUEUE_KEY = "frontend_push_queue_";
	
	/**
	 * 公聊消息信息key前缀
	 * hash结构: room_message_info_{messageid}:{field}:{value}
	 * 例如 hset room_message_info_123 content helloworld
	 */
	public final static String ROOM_MESSAGE_INFO_KEY_PREFIX = "room_message_info_";
	
	/**
	 * 公聊消息列表key前缀
	 * zset结构:  room_message_list_{roomid}:{messageid}
	 * 例如: zset room_message_list_1 score 123
	 */
	public final static String ROOM_MESSAGE_LIST_KEY_PREFIX = "room_message_list_";
	
	/**
	 * 私聊消息信息key前缀
	 * hash结构: private_message_info_{messageid}:{field}:{value}
	 * 例如 hset private_message_info_123 content helloworld
	 */
	public final static String PRIVATE_MESSAGE_INFO_KEY_PREFIX = "private_message_info_";
	
	/**
	 * 私聊消息列表key前缀
	 * zset结构:  private_message_list_{minuserid_maxuserid}:{messageid}
	 * 例如: zset private_message_list_111_222 score 123
	 */
	public final static String PRIVATE_MESSAGE_LIST_KEY_PREFIX = "private_message_list_";
	
	/**
	 * 用户私聊的未读数key前缀
	 * hash结构: user_private_unread_{touserid}:{fromuserid}:{unread_count}
	 * 例如: hset user_private_unread_111 222 {"from_uid" : 111, "content":"xxx", "is_show_notify": true, "click_act":"xxx"}
	 */
	public final static String USER_PRIVATE_UNREAD_KEY_PREFIX = "user_private_unread_";
	
	/**
	 * 用户好友申请/处理结果通知key前缀
	 * hash结构: user_friend_notify_{touserid}:{fromuserid}:{message}
	 * 例如: hset user_friend_notify_111 222 {"from_uid" : 111, "content":"xxx", "is_show_notify": true, "click_act":"xxx"}
	 */
	public final static String USER_FRIEDN_NOTIFY_KEY_PREFIX = "user_friend_notify_";
	
	/**
	 * 用户对应的Frontend地址
	 * hash结构 hset uid_frontend 111 192.168.1.1
	 */
	public final static String UID_FRONTEND_KEY_PREFIX = "uid_frontend_";
	
	/**
	 * 房间最后一条消息的时间戳
	 * string 结构 room_last_time_stamp_{roomid} : timestamp
	 * 例如 set room_last_time_stamp_1 131213434343
	 * 该结构用于返回房间是否有新消息的通知
	 */
	public final static String ROOM_LAST_TIME_STAMP_KEY_PREFIX = "room_last_time_stamp_";
	
	/**
	 * 用户是否有权限给其他用户发送私聊消息
	 * string 结构 user_allow_send_touid_{uid_touid} true
	 * 例如 set user_allow_send_touid_1_2 true
	 * 过期时间30秒
	 */
	public final static String USER_ALLOW_SEND_TOUID_KEY_PREFIX = "user_allow_send_touid_";
	
	/**
	 * 进入房间的uid集合
	 * hash 结构 enter_room_uid_set_{roomid} {uid...} {enter_timestamp}
	 * 例如 hset enter_room_uid_set_1 1111 138173432323
	 * 该结构用于向进入房间的用户实时push消息以及返回进入房间的用户总数
	 */
	public final static String ENTER_ROOM_UID_SET_KEY_PREFIX = "enter_room_uid_set_";
	
	/**
	 * 订阅房间的uid集合
	 * zset 结构 subscribe_room_uid_set_{roomid} {score} {uid...}
	 * 例如 zadd subscribe_room_uid_set_1 10 1111
	 * 该结构用于客户端展示房间的活跃用户，需要设置上限
	 * redis只存储房间一部分用户(按发消息数倒序), mysql存储房间全部用户
	 * 同步数据时, 服务端操作步骤如下:
	 *     1. 接口将用户订阅的房间列表上传到服务端
	 *     2. 先取出用户原有的房间列表, 遍历该房间列表，判断房间是否还在订阅的房间列表中
	 *     3. 如果还存在，则取出用户在该房间中的排序值，如果不存在，则不处理
	 *     4. 将用户对应的房间清空
	 *     5. 将新订阅的房间列表逐一添加进去(如果有排序值，则需要附带排序值添加)
	 */
	public final static String SUBSCRIBE_ROOM_UID_SET_KEY_PREFIX = "subscribe_room_uid_set_";
	
	/**
	 * 用户信息
	 * hash 结构 hset user_info_{uid} {field} {value}
	 * 例如 hset user_info_1111 nick_name milo
	 * 需要过期时间
	 */
	public final static String USER_INFO_KEY_PREFIX = "user_";
	
	/**
	 * 用户原子封装信息
	 * hash 结构 hset user_atom_{uid} {field} {value}
	 * 例如 hset user_atom_111 platform android
	 * 当握手时需要重置该信息
	 */
	public final static String USER_ATOM_KEY_PREFIX = "user_atom_";
	
	/**
	 * IPHONE用户的token信息
	 * String 结构 set user_token_{uid} {token}
	 * 例如 set user_token_111 abcdedfg
	 * 当握手时需要重置该信息, 当用户注销时删除 
	 */
	public final static String USER_TOKEN_KEY_PREFIX = "user_token_";
	
	/**
	 * 群聊消息自增ID rediskey
	 */
	public final static String GROUP_MESSAGE_ID_INCREMENT_KEY = "group_message_id";
	
	/**
	 * 群聊消息信息key前缀
	 * hash结构: group_message_info_{messageid}:{field}:{value}
	 * 例如 hset group_message_info_123 content helloworld
	 */
	public final static String GROUP_MESSAGE_INFO_KEY_PREFIX = "group_message_info_";
	
	/**
	 * 群聊消息列表key前缀
	 * zset结构:  group_message_list_{roomid}:{messageid}
	 * 例如: zset group_message_list_1 score 123
	 */
	public final static String GROUP_MESSAGE_LIST_KEY_PREFIX = "group_message_list_";
	
	/**
	 * 公会群组用户列表key前缀
	 */
	public final static String GUILD_GROUP_USER_LIST_KEY_PREFIX = "guild_group_user_list_";
	
	/**
	 * 公会群组信息key前缀
	 */
	public final static String GUILD_GROUP_INFO_KEY_PREFIX = "guild_group_info_";
	
	/**
	 * 公会信息key前缀
	 */
	public final static String GUILD_INFO_KEY_PREFIX = "guild_info_";
	
	/**
	 * 用户私聊的未读数key前缀
	 * hash结构: user_group_unread_{userid}:{groupid}:{unread_count}
	 * 例如: hset user_group_unread_111 222 {"from_uid" : 111, "content":"xxx", "is_show_notify": true, "click_act":"xxx"}
	 */
	public final static String USER_GROUP_UNREAD_KEY_PREFIX = "user_group_unread_";
	
	public final static String POST_REPLY_NOTIFY_ID_NCREMENT_KEY = "post_reply_notify_id";
	
	public final static String POST_REPLY_NOTIFY_INFO_KEY_PREFIX = "post_reply_notify_info_";
	
	public final static String USER_POST_REPLY_NOTIFY_LIST_KEY_PREFIX = "user_post_reply_notify_list_";
	
	public final static String SYS_MESSAGE_NOTIFY_ID_NCREMENT_KEY = "sys_message_notify_id";
	
	public final static String SYS_MESSAGE_NOTIFY_INFO_KEY_PREFIX = "sys_message_notify_info_";
	
	public final static String USER_SYS_MESSAGE_NOTIFY_LIST_KEY_PREFIX = "user_sys_message_notify_list_";
	
	public final static String FEED_RECOMMEND_NOTIFY_ID_NCREMENT_KEY = "feed_recommend_notify_id";
	
	public final static String FEED_RECOMMEND_NOTIFY_INFO_KEY_PREFIX = "feed_recommend_notify_info_";
	
	public final static String USER_FEED_RECOMMEND_NOTIFY_LIST_KEY_PREFIX = "user_feed_recommend_notify_list_";
}