package com.mofang.chat.business.entity;

import org.json.JSONObject;

import com.mofang.chat.business.sysconf.common.UserStatus;

/**
 * 
 * @author zhaodx
 *
 */
public class Atom
{
	private String sessionId;
	private long userId;
	private String platform;
	private String deviceToken;
	private int status;
	
	public String getSessionId()
	{
		return sessionId;
	}
	
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
	
	public long getUserId()
	{
		return userId;
	}
	
	public void setUserId(long userId)
	{
		this.userId = userId;
	}
	
	public String getPlatform()
	{
		return platform;
	}
	
	public void setPlatform(String platform)
	{
		this.platform = platform;
	}
	
	public String getDeviceToken() 
	{
		return deviceToken;
	}
	
	public void setDeviceToken(String deviceToken)
	{
		this.deviceToken = deviceToken;
	}
	
	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("id", userId);
			json.put("sid", sessionId == null ? "" : sessionId);
			json.put("pf", platform == null ? "" : platform);
			json.put("token", deviceToken == null ? "" : deviceToken);
			json.put("status", status);
			return json;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static Atom buildByJson(JSONObject json)
	{
		Atom model = new Atom();
		try
		{
			model.setUserId(json.optLong("id", 0L));
			model.setSessionId(json.optString("sid", ""));
			model.setPlatform(json.optString("pf", ""));
			model.setDeviceToken(json.optString("token", ""));
			model.setStatus(json.optInt("status", UserStatus.NORMAL));
			return model;
		}
		catch(Exception e)
		{
			return null;
		}
	}
}