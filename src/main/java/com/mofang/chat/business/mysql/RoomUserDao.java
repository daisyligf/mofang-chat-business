package com.mofang.chat.business.mysql;

import java.util.List;

import com.mofang.chat.business.model.RoomUser;

/**
 * 
 * @author zhaodx
 *
 */
public interface RoomUserDao
{
	public boolean add(RoomUser model) throws Exception;
	
	public boolean updateMsgCount(RoomUser model) throws Exception;
	
	public boolean delete(int roomId, long userId) throws Exception;
	
	public List<RoomUser> getUsers(int roomId) throws Exception;
	
	public List<RoomUser> getUsersOrderByCount(int roomId, long limit) throws Exception;
	
	public List<RoomUser> getRooms(long userId) throws Exception;
	
	public List<Integer> getAllRooms() throws Exception;
}