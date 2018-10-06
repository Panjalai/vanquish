package com.rbs.vanquish.framework.bpm.pool;

import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import com.rbs.vanquish.framework.bpm.pool.common.Poolable;

public class BPMProcess extends Thread implements Poolable{
	
	private RuntimeService runtimeService = null;
	private String businessProcessID = null;
	private String uniqueThreadID = null;
	private String uniqueBusinessKey = null;
	private Map<String,Object> processVaraiablesMap = null;
	
	public BPMProcess (RuntimeService aRuntimeService, String aBusinessProcessID, String aUniqueBusinessKey, Map<String,Object> aProcessVaraiablesMap) {
		this.runtimeService = aRuntimeService;
		this.businessProcessID = aBusinessProcessID;
		this.uniqueBusinessKey = aUniqueBusinessKey;
		this.processVaraiablesMap = aProcessVaraiablesMap;
	}
	

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}



	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}



	public String getUniqueThreadID() {
		return uniqueThreadID;
	}



	public void setUniqueThreadID(String uniqueThreadID) {
		this.uniqueThreadID = uniqueThreadID;
	}



	public String getUniqueBusinessKey() {
		return uniqueBusinessKey;
	}



	public void setUniqueBusinessKey(String uniqueBusinessKey) {
		this.uniqueBusinessKey = uniqueBusinessKey;
	}



	public Map<String, Object> getProcessVaraiablesMap() {
		return processVaraiablesMap;
	}



	public void setProcessVaraiablesMap(Map<String, Object> processVaraiablesMap) {
		this.processVaraiablesMap = processVaraiablesMap;
	}



	public String getBusinessProcessID() {
		return businessProcessID;
	}


	public void setBusinessProcessID(String businessProcessID) {
		this.businessProcessID = businessProcessID;
	}


	@Override
	public void run() 
	{
		String lsPorcessID = exceuteBusinessProcess();
	    logMessage("New Process Created with ID -> "+lsPorcessID);
		storeProcessIDInDatabase(lsPorcessID);
	}//eof aRuntimeService
	
	/**
	 * 
	 * @param aProcessID
	 */
	private void storeProcessIDInDatabase(String aProcessID) {
		logMessage ("   >> storeProcessIDInDatabase () ");
		logMessage ("   << storeProcessIDInDatabase () ");
	}//eof storeProcessIDInDatabase
	
	
	/**
	 * 
	 * @return
	 */
	private String exceuteBusinessProcess() 
	{
		logMessage ("   >> exceuteBusinessProcess () ");
		ProcessInstance loProcessInstance =  getRuntimeService().startProcessInstanceByKey(getBusinessProcessID(),
		getUniqueBusinessKey(), getProcessVaraiablesMap());
		
	    String lsProcessID = loProcessInstance.getProcessInstanceId();

	    logMessage ("   << exceuteBusinessProcess () ");
	    return lsProcessID;
	}//eof exceuteprocess
	
	
	 private static void logMessage(String lsMessage) {
		 System.out.println(lsMessage);
	 }

}
