package com.zy.util;

import com.google.gson.Gson;

public class JsonUtil
{
	public static String serialize(Object t)
	{
		Gson gson = new Gson();
		return gson.toJson(t);
	}
	
	public static <T> T deserialize(String json, Class<T> clazz)
	{
		Gson gson = new Gson();
		return gson.fromJson(json, clazz);
	}
}
