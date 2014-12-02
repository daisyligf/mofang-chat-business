package com.mofang.chat.business.redis;

/**
 * 
 * @author zhaodx
 *
 */
public interface WriteQueueRedis 
{
	/**
	 * 将数据放入写队列
	 * @param task 待处理任务
	 * @return
	 * @throws Exception
	 */
	public boolean put(String task) throws Exception;
	
	/**
	 * 从写队列获取数据
	 * @return
	 * @throws Exception
	 */
	public String get() throws Exception;
	
	/**
	 * 将apple通知放入队列
	 * @param notify
	 * @return
	 * @throws Exception
	 */
	public boolean putAppleNotify(String notify) throws Exception;
}