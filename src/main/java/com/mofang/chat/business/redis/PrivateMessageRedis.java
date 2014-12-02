package com.mofang.chat.business.redis;

import java.util.Set;

import com.mofang.chat.business.model.ChatPrivateMessage;

/**
 * 
 * @author zhaodx
 *
 */
public interface PrivateMessageRedis
{	
	/**
	 * 获取私聊消息最大ID
	 * @return
	 * @throws Exception
	 */
	public Long getMaxMessageId() throws Exception;
	
	/**
	 * 保存私聊消息
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean save(ChatPrivateMessage model, int maxCount) throws Exception;
	
	/**
	 * 删除私聊消息(用于将过期的消息删除)
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean delete(final ChatPrivateMessage model) throws Exception;
	
	/**
	 * 获取私聊消息信息
	 * @param messageId
	 * @return
	 */
	public ChatPrivateMessage getInfo(long messageId);
	
	/**
	 * 获取私聊消息列表
	 * @param roomId
	 * @param maxMessageId
	 * @param minMessageId
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Set<String> getList(long fromUserId, long toUserId, long minMessageId, long maxMessageId, int pageSize) throws Exception;
}