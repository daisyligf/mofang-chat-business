package com.mofang.chat.business.sysconf;

public class ReturnCode
{
	/**
	 * 操作成功
	 */
	public final static int SUCCESS = 0;
	
	/**
	 * 无效参数
	 */
	public final static int CLIENT_REQUEST_DATA_IS_INVALID = 400;
	
	/**
	 * 请求参数格式不正确
	 */
	public final static int CLIENT_REQUEST_PARAMETER_FORMAT_ERROR = 401;
	
	/**
	 * 请求缺少必要参数
	 */
	public final static int CLIENT_REQUEST_LOST_NECESSARY_PARAMETER = 402;
	
	/**
	 * 服务器错误
	 */
	public final static int SERVER_ERROR = 500;
	
	/**
	 * 进入房间鉴权失败
	 */
	public final static int ENTER_ROOM_AUTH_FAIL = 901;
	
	/**
	 * 用户没有权限给目标用户发送私聊信息
	 */
	public final static int USER_NOT_HAVE_PRIVILEGE_TO_SEND_MESSAGE = 902;
}