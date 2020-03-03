package kr.ac.ync.its.controller;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ItsJsonBuilder {
	JSONArray jsonArr;
	JSONObject jsonObj;
	String jsonStr;
	HashMap<String,Object> jsonHashMap;
	public ItsJsonBuilder() {
		jsonArr = new JSONArray();
		jsonObj = new JSONObject();
		jsonStr = new String();
		jsonHashMap = new HashMap<String,Object>();
	}
	
	public ItsJsonBuilder(JSONArray jArray) {
		jsonArr = jArray;
	}
	
	
	public String objToString() {
		return jsonObj.toJSONString();
	}
	public String arrToString() {
		return jsonArr.toJSONString();
	}
	
	public void clearAll() {
		clearHashMap();
		clearJsonObj();
		clearJsonArr();
		clearStr();
	}
	public void clearHashMap() {
		jsonHashMap = new HashMap<String,Object>();
	}
	public void clearJsonObj() {
		jsonObj = new JSONObject();
	}
	public void clearJsonArr() {
		jsonArr = new JSONArray();
	}
	public void clearStr() {
		jsonStr = new String();
	}
	
	public void put(String key, Object value) {
		jsonHashMap.put(key, value);
	}
	
	public void buildObj() {
		jsonObj = new JSONObject(jsonHashMap);
	}
	@SuppressWarnings("unchecked")
	public void appendToArr() {
		jsonArr.add(jsonObj);
	}
}
