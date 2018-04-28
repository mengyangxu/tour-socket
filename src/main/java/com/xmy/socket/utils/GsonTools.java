package com.xmy.socket.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class GsonTools {

	public GsonTools() {
		// TODO Auto-generated constructor stub
	}

	public static String createGsonString(Object object) {
		Gson gson = new Gson();
		String gsonString = gson.toJson(object);
		return gsonString;
	}
	
	public static Map<String, String> changeGsonToMap(String data){
       GsonBuilder gb = new GsonBuilder();
       Gson g = gb.create();
       Map<String, String> map = g.fromJson(data, new TypeToken<Map<String, String>>() {}.getType());
       return map;
    }
	
	public static Map<String,Map<String, String>> changeGsonToMapsMaps(String gsonString) {
		Map<String,Map<String, String>> obj = null;
		Gson gson = new Gson();
		obj = gson.fromJson(gsonString,
				new TypeToken<Map<String,Map<String, String>>>(){}.getType());
		return obj;
	}
	
	public static List<Map<String, String>> changeGsonToListMaps(String gsonString) {
		List<Map<String, String>> obj = null;
		Gson gson = new Gson();
		obj = gson.fromJson(gsonString,
				new TypeToken<List<Map<String, String>>>(){}.getType());
		return obj;
	}
	
	public static Map<String, List<Map<String, String>>> changeGsonToMapLists(String gsonString) {
		Map<String, List<Map<String, String>>> obj = null;
		Gson gson = new Gson();
		obj = gson.fromJson(gsonString,
				new TypeToken<Map<String, List<Map<String, String>>>>(){}.getType());
		return obj;
	}
	
	public static Map<String,List<Map<String, String>>> changeGsonToMapListMaps(String gsonString) {
		Map<String,List<Map<String, String>>> obj = null;
		Gson gson = new Gson();
		obj = gson.fromJson(gsonString,
				new TypeToken<Map<String,List<Map<String, String>>>>(){}.getType());
		return obj;
	}
	
	   public static Map<String, Object> changeGsonToObject(String gsonString) {
	        Map<String, Object> obj = null;
	        Gson gson = new Gson();
	        obj = gson.fromJson(gsonString,
	                new TypeToken<Map<String, Object>>(){}.getType());
	        return obj;
	    }


}
