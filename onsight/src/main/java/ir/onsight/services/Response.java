package ir.onsight.services;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Response {
	private boolean result;
	private String message;
	private HashMap<String, Object> objects;
	
	public Response(boolean result,String message) {
		this.result = result;
		this.message = message;
		this.objects = new HashMap<String, Object>();
	}
	public Response(boolean result) {
		this(result, "");
	}
	public Response(boolean result,String message,String objName,Object obj) {
		this(result, message);
		objects.put(objName,obj);
	}
	
	public void addObject(String objName,Object obj){
		objects.put(objName,obj);
	}
	
	public String toJson(){
		Gson gson = new Gson();
		JsonObject responseJson = new JsonObject();
		responseJson.addProperty("result", result);
		if(!message.equals(""))
			responseJson.addProperty("message", message);
		for(String objName : objects.keySet())
			responseJson.add(objName,gson.toJsonTree(objects.get(objName)));		
		return responseJson.toString();
	}
}
