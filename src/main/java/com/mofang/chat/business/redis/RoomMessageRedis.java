package com.mofang.chat.business.redis;

import java.util.Set;

import com.mofang.chat.business.model.ChatRoomMessage;

/**
 * 
 * @author zhaodx
 *
 */
public interface RoomMessageRedis
{
	/**
	 * 获取公聊消息最大ID
	 * @return
	 * @throws Exception
	 */
	public long getMaxMessageId() throws Exception;
	
	/**
	 * 保存公聊消息
	 * @param model
	 * @param maxCount
	 * @return
	 * @throws Exception
	 */
	public boolean save(ChatRoomMessage model, int maxCount) throws Exception;
	
	/**
	 * 获取房间消息列表 <br />
	 * 使用 zrevrangebyscore 命令获取消息集合 <br />
	 * 结构 zrevrangebyscore {key} {maxscore} {minscore} limit {start} {end} <br />
	 * 例如 zrevrangebyscore room_message_list_1 1000 0 limit 0 10 <br />
	 * 示例:  <br />
	 * zadd room_message_list_1 1 A <br />
	 * zadd room_message_list_1 2 B <br />
	 * zadd room_message_list_1 3 C <br />
	 * ...... <br />
	 * zadd room_message_list_1 25 Y <br />
	 * zadd room_message_list_1 26 Z <br />
	 * zrevrangebyscore room_message_list_1 26 0 limit 0 10 <br />
	 * 结果如下: <br />
	 * 1) "Z" <br />
	 * 2) "Y" <br />
	 * 3) "X" <br />
	 * 4) "W" <br />
	 * 5) "V" <br />
	 * 6) "U" <br />
	 * 7) "T" <br />
	 * 8) "S" <br />
	 * 9) "R" <br />
	 * 10) "Q" <br />
	 * 
	 * @param roomId
	 * @param maxMessageId
	 * @param minMessageId
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Set<String> getList(int roomId, long minMessageId, long maxMessageId, int pageSize) throws Exception;
	
	/**
	 * 获取房间消息信息
	 * @param messageId
	 * @return
	 */
	public ChatRoomMessage getInfo(long messageId);
	
	/**
	 * 更新消息状态
	 * @param messageId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public boolean updateStatus(long messageId, int status) throws Exception;
}