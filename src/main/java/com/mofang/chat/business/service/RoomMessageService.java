package com.mofang.chat.business.service;

import java.util.List;

import com.mofang.chat.business.entity.RoomMessage;
import com.mofang.chat.business.entity.RoomMessageCollection;

/**
 * 
 * @author zhaodx
 *
 */
public interface RoomMessageService
{
	public long sendMessage(RoomMessage model) throws Exception;
	
	public RoomMessage getPushNotify(int roomId) throws Exception;
	
	public List<RoomMessage> getPullNotify(List<Integer> roomIds) throws Exception;
	
	public RoomMessageCollection getPullMessages(int roomId, long minMessageId, long maxMessageId, int pageSize) throws Exception;
	
	public boolean updateStatus(long messageId, int status) throws Exception;
}