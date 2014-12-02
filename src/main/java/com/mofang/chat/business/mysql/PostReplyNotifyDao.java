package com.mofang.chat.business.mysql;

import com.mofang.chat.business.model.PostReplyNotify;

/**
 * 
 * @author zhaodx
 *
 */
public interface PostReplyNotifyDao
{
	public boolean add(PostReplyNotify model) throws Exception;
}