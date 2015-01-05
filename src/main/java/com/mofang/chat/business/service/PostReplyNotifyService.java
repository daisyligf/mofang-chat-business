package com.mofang.chat.business.service;

import java.util.List;

import com.mofang.chat.business.model.PostReplyNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface PostReplyNotifyService
{
	public void pushNotify(PostReplyNotify model) throws Exception;
	
	public PostReplyNotify getInfo(long notifyId) throws Exception;
	
	public List<PostReplyNotify> getList(long userId) throws Exception;
	
	public List<PostReplyNotify> getList(long userId, long start, long size) throws Exception;
}