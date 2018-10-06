package com.rbs.vanquish.framework.bpm.config;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;
import com.rbs.vanquish.framework.bpm.exception.VanquishRuntimeException;


public class VanquishConfigManager {
	
	private static VanquishConfigManager instance = null;
	private static Map<String,String> taskMap = new HashMap<String,String>();
	private static Map<String,String> eventMap = new HashMap<String,String>();
	private static Map<String,String> queueMessageNameMap = new HashMap<String,String>();
	private static String EMPTY_STRING = "";
	
	private VanquishConfigManager() throws VanquishApplicationException {
		try
		{
			ClassLoader classLoader = this.getClass().getClassLoader();
			File loFile = new File(classLoader.getResource("vanquish.json").getFile());
			
			JSONParser loParser = new JSONParser(); 
			JSONObject loJSONObject = (JSONObject) loParser.parse(new FileReader(loFile));
			
			JSONObject loVanObject = (JSONObject) loJSONObject.get("Vanquish_Application");
			JSONArray loTasks = (JSONArray) loVanObject.get("task");
			
			Iterator<JSONObject> loTaskIter = loTasks.iterator();
	        while (loTaskIter.hasNext()) {
	        	JSONObject loTask = loTaskIter.next();
	        	String lsTaskName = (String) loTask.get("taskName");
	        	String lsJavaClass = (String) loTask.get("class");
	        	logMessage("lsTaskName -> "+lsTaskName);
	        	logMessage("lsJavaClass -> "+lsJavaClass);
	        	if (lsTaskName == null) lsTaskName = EMPTY_STRING;
	        	if (lsJavaClass == null) lsJavaClass = EMPTY_STRING;
	        	lsTaskName = lsTaskName.trim();
	        	lsJavaClass = lsJavaClass.trim();
	        	taskMap.put(lsTaskName, lsJavaClass);
	        }//eof loTaskIter
	        
	    	JSONArray loQueues = (JSONArray) loVanObject.get("queue");
	        Iterator<JSONObject> loQueueIter = loQueues.iterator();
	        while (loQueueIter.hasNext()) {
	        	JSONObject loQueue = loQueueIter.next();
	        	String lsQueueName = (String) loQueue.get("queueName");
	        	String lsMessageName = (String) loQueue.get("MessageName");
	        	logMessage("lsQueueName -> "+lsQueueName);
	        	logMessage("lsMessageName -> "+lsMessageName);
	        	if (lsQueueName == null) lsQueueName = EMPTY_STRING;
	        	if (lsMessageName == null) lsMessageName = EMPTY_STRING;
	        	lsQueueName = lsQueueName.trim();
	        	lsMessageName = lsMessageName.trim();
	        	queueMessageNameMap.put(lsQueueName, lsMessageName);
	        }//eof loQueueIter
	        
	        JSONArray loEvents = (JSONArray) loVanObject.get("event");
			
			Iterator<JSONObject> loEventIter = loEvents.iterator();
	        while (loEventIter.hasNext()) {
	        	JSONObject loEvent = loEventIter.next();
	        	String lsMessageName = (String) loEvent.get("MessageName");
	        	String lsJavaClass = (String) loEvent.get("class");
	        	logMessage("lsMessageName -> "+lsMessageName);
	        	logMessage("lsJavaClass -> "+lsJavaClass);
	        	if (lsMessageName == null) lsMessageName = EMPTY_STRING;
	        	if (lsJavaClass == null) lsJavaClass = EMPTY_STRING;
	        	lsMessageName = lsMessageName.trim();
	        	lsJavaClass = lsJavaClass.trim();
	        	eventMap.put(lsMessageName, lsJavaClass);
	        }//eof loEventIter
		}
		catch (Exception aExcepton)
		{
			aExcepton.printStackTrace();
			throw new VanquishRuntimeException(aExcepton);
		}
	}//eof VanquishConfigManager

	   public static VanquishConfigManager getInstance() throws VanquishApplicationException{
	      if(instance == null) {
	         instance = new VanquishConfigManager();
	      }
	      return instance;
	   }//eof VanquishConfigManager
	   
	   public String getJavaClassForTask(String aTaskID) {
		   if (aTaskID == null) aTaskID = EMPTY_STRING;
		   return taskMap.get(aTaskID);
	   }
	   
	   public String getMessageNameForQueue(String aQueueName) {
		   if (aQueueName == null) aQueueName = EMPTY_STRING;
		   return queueMessageNameMap.get(aQueueName);
	   }
	   
	   public String getJavaClassForEvent(String aEventID) {
		   if (aEventID == null) aEventID = EMPTY_STRING;
		   return eventMap.get(aEventID);
	   }
	   
	   private static void logMessage(String lsMessage) {
			//System.out.println(lsMessage);
		}

}
