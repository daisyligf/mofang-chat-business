package com.mofang.chat.business.redis;

import java.util.Map;

import com.mofang.chat.business.entity.Atom;
import com.mofang.chat.business.entity.FriendMessage;
import com.mofang.chat.business.entity.PrivateMessage;
import com.mofang.chat.business.entity.User;

/**
 * 
 * @author zhaodx
 *
 */
public interface UserRedis
{
	public boolean saveInfo(User user, int expireSeconds) throws Exception;
	
	public boolean removeInfo(long userId) throws Exception;
	
	public User getInfo(long userId) throws Exception;
	
	public boolean setAllowSend(long fromUserId, long toUserId, int expireSeconds) throws Exception;
	
	public boolean getAllowSend(long fromUserId, long toUserId) throws Exception;
	
	public boolean setFrontend(long userId, String serverIp) throws Exception;
	
	public boolean removeFrontend(long userId) throws Exception;
	
	public String getFrontend(long userId) throws Exception;
	
	public boolean incrUnreadCount(PrivateMessage model) throws Exception;
	
	public boolean removeUnreadCount(long fromUserId, long toUserId) throws Exception;
	
	public PrivateMessage getUnreadCount(long fromUserId, long toUserId) throws Exception;
	
	public Map<Long, PrivateMessage> getUnreadCounts(long userId) throws Exception;
	
	public boolean setFriendNotify(FriendMessage model) throws Exception;
	
	public boolean removeFriendNotify(long fromUserId, long toUserId) throws Exception;
	
	public FriendMessage getFriendNotify(long fromUserId, long toUserId) throws Exception;
	
	public boolean setAtom(Atom atom) throws Exception;
	
	public boolean removeAtom(long userId) throws Exception;
	
	public Atom getAtom(long userId) throws Exception;
	
	public boolean setToken(long userId, String token) throws Exception;
	
	public boolean removeToken(long userId) throws Exception;
	
	public String getToken(long userId) throws Exception;
}