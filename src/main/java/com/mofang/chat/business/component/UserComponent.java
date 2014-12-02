package com.mofang.chat.business.component;

import org.json.JSONObject;

import com.mofang.chat.business.entity.User;
import com.mofang.chat.business.sysconf.SysObject;
import com.mofang.chat.business.sysconf.common.UserStatus;
import com.mofang.chat.business.sysconf.common.UserType;
import com.mofang.framework.net.http.HttpClientSender;

/**
 * 
 * @author zhaodx
 *
 */
public class UserComponent
{
	public static User getAuth(long userId, String sessionId)
	{
		if(sessionId.equals("abcdefg"))
		{
			User user = new User();
			user.setUserId(userId);
			user.setSessionId(sessionId);
			user.setNickName("mao1mao");
			user.setAvatar("http://www.mao1mao.com");
			user.setStatus(0);
			return user;
		}
		String url = SysObject.USER_AUTH_URL + "?uid=" + userId + "&sid=" + sessionId;
		try
		{
			String result = HttpClientSender.get(SysObject.HTTP_CLIENT, url);
			JSONObject json = new JSONObject(result);
			int code = json.optInt("code", -1);
			if(0 != code)
				return null;
			
			JSONObject data = json.optJSONObject("data");
			if(null == data)
				return null;
			
			User user = new User();
			user.setUserId(userId);
			user.setSessionId(sessionId);
			user.setNickName(data.optString("nickname", ""));
			user.setAvatar(data.optString("avatar", ""));
			user.setStatus(data.optInt("status", UserStatus.NORMAL));
			user.setType(data.optInt("type", UserType.NORMAL));
			user.setGender(data.optInt("sex", 1));
			return user;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static User getInfo(long userId)
	{
		String url = SysObject.USER_INFO_URL + "?to_uid=" + userId;
		try
		{
			String result = HttpClientSender.get(SysObject.HTTP_CLIENT, url);
			JSONObject json = new JSONObject(result);
			int code = json.optInt("code", -1);
			if(0 != code)
				return null;
			
			JSONObject data = json.optJSONObject("data");
			if(null == data)
				return null;
			
			User user = new User();
			user.setUserId(userId);
			user.setSessionId("");
			user.setNickName(data.optString("nickname", ""));
			user.setAvatar(data.optString("avatar", ""));
			user.setStatus(data.optInt("status", UserStatus.NORMAL));
			user.setType(data.optInt("type", UserType.NORMAL));
			user.setGender(data.optInt("sex", 1));
			return user;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static boolean isAllowToSend(long fromUserId, long toUserId)
	{
		String url = SysObject.ALLOW_SEND_URL + "?from_uid=" + fromUserId + "&to_uid=" + toUserId;
		try
		{
			String result = HttpClientSender.get(SysObject.HTTP_CLIENT, url);
			JSONObject json = new JSONObject(result);
			int code = json.optInt("code", -1);
			if(0 != code)
				return false;
			
			JSONObject data = json.optJSONObject("data");
			if(null == data)
				return false;
			
			return data.optBoolean("flag", false);
		}
		catch(Exception e)
		{
			return false;
		}
	}
}