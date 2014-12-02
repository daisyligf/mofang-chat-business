package com.mofang.chat.business.sysconf;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class ResultValue
{
	private String action;
	private Integer code;
	private Long sequenceId;
	private Object data;
	
	public String getAction()
	{
		return action;
	}
	
	public void setAction(String action) 
	{
		this.action = action;
	}
	
	public Integer getCode() 
	{
		return code;
	}
	
	public void setCode(Integer code)
	{
		this.code = code;
	}
	
	public Long getSequenceId()
	{
		return sequenceId;
	}
	
	public void setSequenceId(Long sequenceId) 
	{
		this.sequenceId = sequenceId;
	}
	
	public Object getData()
	{
		return data;
	}
	
	public void setData(Object data) 
	{
		this.data = data;
	}
	
	public String toJsonString()
	{
		JSONObject json = new JSONObject();
		try
		{
			if(!StringUtil.isNullOrEmpty(action))
				json.put("act", action);
			if(null != code)
				json.put("code", code);
			if(null != sequenceId)
				json.put("seq_id", sequenceId);
			if(null != data)
			{
				if(data instanceof JSONObject)
					json.put("data", (JSONObject)data);
				else if(data instanceof JSONArray)
					json.put("data", (JSONArray)data);
			}
			return json.toString();
		}
		catch(Exception e)
		{
			return "{\"code\" : 500}";
		}
	}
}