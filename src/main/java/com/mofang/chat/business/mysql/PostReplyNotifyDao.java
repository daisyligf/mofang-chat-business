package com.mofang.chat.business.mysql;

import java.util.List;

import com.mofang.chat.business.model.PostReplyNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface PostReplyNotifyDao
{
	public boolean add(PostReplyNotify model) throws Exception;
	
	public void updateStatusByUserId(long userId, int status) throws Exception;
	
	public List<PostReplyNotify> getList(long userId, long start, long size) throws Exception;
	
	public long getCount(long userId) throws Exception;
}