package com.mofang.chat.business.service;

import java.util.List;
import java.util.Set;

import com.mofang.chat.business.entity.User;

/**
 * 
 * @author zhaodx
 *
 */
public interface RoomService
{
	public boolean subscribe(long userId, Set<Integer> roomSet) throws Exception;
	
	public List<User> getSubscribeUsers(int roomId, int start, int end) throws Exception;
}