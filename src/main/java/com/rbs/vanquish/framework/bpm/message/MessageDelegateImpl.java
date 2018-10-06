package com.rbs.vanquish.framework.bpm.message;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.EventSubscription;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
//import javax.jms.Message;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.rbs.vanquish.framework.bpm.common.MessageExceutionContextImpl;
import com.rbs.vanquish.framework.bpm.common.MessageExecutionContext;
import com.rbs.vanquish.framework.bpm.config.VanquishConfigManager;
import com.rbs.vanquish.framework.bpm.constant.VanquishConstants;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;


public class MessageDelegateImpl implements MessageDelegate {
	
	public MessageDelegateImpl()
	{
	}
	
	public MessageDelegateImpl(ProcessEngine aProcessEngine)
	{
		processEngine = aProcessEngine;
	}
	
	private ProcessEngine processEngine;
	
	
	
	private void delegateAndExecuteMessage(String aMessageName, Message aMessage) throws Exception {
		
		 logMessage(" >> delegateAndExecuteMessage >> ");
		 logMessage(" aMessageName -> "+aMessageName);
		
		 //Process ID should be fetched form variables or through REST Services (Each File should
		 //have corresponding Process ID)
		 //String lsProcessInstanceID = aProcessInstanceID;
		 //logMessage(" lsProcessInstanceID -> "+lsProcessInstanceID);
		 
		 //ProcessInstance loProcessInstance =  runtimeService.startProcessInstanceById(lsProcessInstanceID);
		 
		// EventSubscription loSubscription = runtimeService.createEventSubscriptionQuery().processInstanceId(lsProcessInstanceID).
		// eventType("message").eventName(aMessageName).singleResult();
		 
		 //runtimeService.startProcessInstanceByKey("1234589ghcve23468dfssg").
		 
		// Execution execution = runtimeService.createExecutionQuery().processInstanceId(aProcessInstanceID)
		//	      .processVariableValueEquals("uniqueRefrenceId", "1234589ghcve23468dfssg")
		//	      .singleResult();
		 
		
		 
		 //lsProcessInstanceID = execution.getProcessInstanceId();
		 //String lsExceutionID = execution.getId();
		 
		 //logMessage(" lsProcessInstanceID -> "+lsProcessInstanceID);
		 //logMessage(" lsExceutionID       -> "+lsExceutionID);
		 
		 
		 //logMessage(" ExecutionID -> "+loSubscription.getExecutionId());
		 
		 //HashMap<String,Object> loVariablesMap = (HashMap<String, Object>) runtimeService.getVariables(lsExceutionID);
		 
		 //logMessage(" loVariablesMap -> "+loVariablesMap);
		 
		 //Now delegate to the implementation class for execution. VanquishMessageDelegateExecutor identifies the 
		 //Class to be executed based on the given message name by inspecting vanquish.json configuration file
		 
		 HashMap<String,Object> loVariablesMap = new HashMap<String,Object>();
		 
		 MessageExecutionContext loMessageExecutionContext = new MessageExceutionContextImpl(loVariablesMap, aMessageName, null);
		 
		 MessageDelegateProcessor loMessageDelegateProcessor = new VanquishMessageDelegateExecutor();
		 
		 loMessageDelegateProcessor.executeMessage(loMessageExecutionContext, aMessage);
		 
		 //Post Execution; get the response objects and set it back to business process context for subsequent task
		 //to use.
		 HashMap<String,Object> loResponseMap = (HashMap<String, Object>) loMessageExecutionContext.getVariables();
		 
		 logMessage(" loResponseMap -> "+loResponseMap);
		 logMessage(" aMessageName -> "+aMessageName);
		 
		 //Thread.sleep(10000);
		 //String lsBusinessKey = aMessage.getPayload().toString();
		 String lsBusinessKey = null;
		 if (lsBusinessKey != null) lsBusinessKey = lsBusinessKey.trim();
		 logMessage(" lsBusinessKey -> "+lsBusinessKey);
		 
		 RuntimeService loRuntimeService = processEngine.getRuntimeService();
		 
		 logMessage(" loRuntimeService -> "+loRuntimeService);
		 
		 
		 loRuntimeService.createMessageCorrelation(aMessageName).processInstanceBusinessKey(lsBusinessKey)
	        .setVariables(loResponseMap)
	        .correlateWithResult();
		 
		 //Inform BPM to signal the completion of message arrived
		 // runtimeService.messageEventReceived(aMessageName, lsExceutionID, loResponseMap);
		 
		 logMessage(" << delegateAndExecuteMessage << ");
	}//delegateAndExecuteMessage
	
	/**
	 * 
	 * @param aMessageName
	 * @param aMessage
	 * @param aProcessInstanceID
	 * @throws Exception
	 */
	private void delegateAndExecuteMessage(String aMessageName, Message aMessage, String aProcessInstanceID) throws Exception {
		
		 logMessage(" >> delegateAndExecuteMessage >> ");
		 logMessage(" aMessageName -> "+aMessageName);
		
		 //Process ID should be fetched form variables or through REST Services (Each File should
		 //have corresponding Process ID)
		 String lsProcessInstanceID = aProcessInstanceID;
		 logMessage(" lsProcessInstanceID -> "+lsProcessInstanceID);
		 
		 String lsBusinessKey = aMessage.getPayload().toString();
		 //String lsBusinessKey = "";
		 if (lsBusinessKey != null) lsBusinessKey = lsBusinessKey.trim();
		 logMessage(" lsBusinessKey -> "+lsBusinessKey);
		 
		 RuntimeService loRuntimeService = processEngine.getRuntimeService();
		 
		 logMessage(" loRuntimeService -> "+loRuntimeService);
		 
		 EventSubscription loSubscription = loRuntimeService.createEventSubscriptionQuery().processInstanceId(lsProcessInstanceID).
		 eventType("message").eventName(aMessageName).singleResult();
		 
		 logMessage(" ExecutionID -> "+loSubscription.getExecutionId());
		 
		 HashMap<String,Object> loVariablesMap = (HashMap<String, Object>) loRuntimeService.getVariables(loSubscription.getExecutionId());
		 
		 //Now delegate to the implementation class for execution. VanquishMessageDelegateExecutor identifies the 
		 //Class to be executed based on the given message name by inspecting vanquish.json configuration file
		 
		 MessageExecutionContext loMessageExecutionContext = new MessageExceutionContextImpl(loVariablesMap,aMessageName, null);
		 MessageDelegateProcessor loMessageDelegateProcessor = new VanquishMessageDelegateExecutor();
		 
		 loMessageDelegateProcessor.executeMessage(loMessageExecutionContext, aMessage);
		 
		 //Post Execution; get the response objects and set it back to business process context for subsequent task
		 //to use.
		 HashMap<String,Object> loResponseMap = (HashMap<String, Object>) loMessageExecutionContext.getVariables();
		 
		 //Inform BPM to signal the completion of message arrived
		 loRuntimeService.messageEventReceived(aMessageName, loSubscription.getExecutionId(), loResponseMap);
		 
		 logMessage(" << delegateAndExecuteMessage << ");
	}//delegateAndExecuteMessage
	
	private void logMessage(String lsMessage) {
		System.out.println(lsMessage);
	}


	@Override
	public void processMessageWithBusinessKey(String aQueueName, Message aMessage, String aProcessBusinessKey)
	throws VanquishApplicationException 
	{
		logMessage("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		 logMessage(" >> processMessageWithBusinessKey >> ");
		 VanquishConfigManager loVanquishConfigManager = VanquishConfigManager.getInstance();
		 String lsMessageName = loVanquishConfigManager.getMessageNameForQueue(aQueueName);
		 logMessage(" aQueueName         -> "+aQueueName);
		 logMessage(" lsMessageName      -> "+lsMessageName);
		 logMessage(" aProcessBusinessKey -> "+aProcessBusinessKey);
		 delegateAndExecuteMessageWithBusinessKey(lsMessageName,aMessage, aProcessBusinessKey);
		 logMessage(" << processMessageWithBusinessKey << ");
		 logMessage("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}//eof processMessageWithBusinessKey
	
	/**
	 * 
	 * @param aMessageName
	 * @param aMessage
	 * @param aProcessBusinessKey
	 * @throws Exception
	 */
	public void delegateAndExecuteMessageWithBusinessKey(String aMessageName, Message aMessage, String aProcessBusinessKey) 
	throws VanquishApplicationException 
	{
		
		 logMessage(" >> delegateAndExecuteMessageWithBusinessKey >> ");
		 
		 
		 logMessage(" aMessageName        -> "+aMessageName);
		 logMessage(" aProcessBusinessKey -> "+aProcessBusinessKey);
		 
		 RuntimeService loRuntimeService = processEngine.getRuntimeService();
		 logMessage(" loRuntimeService -> "+loRuntimeService);
		 
		 //ProcessInstance loProcessInstance = loRuntimeService.startProcessInstanceByMessage(aMessageName, aProcessBusinessKey);
		 
		 //Execution loExecution = loRuntimeService.createExecutionQuery().processInstanceId(loProcessInstance.getProcessInstanceId())
		 //.processVariableValueEquals(VanquishConstants.UNIQUE_REFERENCE_KEY, aProcessBusinessKey)
		 //.singleResult();
		
		 
		 HashMap<String,Object> loVariablesMap = new HashMap<String,Object>();
		 
		 MessageExecutionContext loMessageExecutionContext = new MessageExceutionContextImpl(loVariablesMap, aMessageName, aProcessBusinessKey);
		 
		 MessageDelegateProcessor loMessageDelegateProcessor = new VanquishMessageDelegateExecutor();
		 
		 loMessageDelegateProcessor.executeMessage(loMessageExecutionContext, aMessage);
		 
		 //Post Execution; get the response objects and set it back to business process context for subsequent task
		 //to use.
		 HashMap<String,Object> loResponseMap = (HashMap<String, Object>) loMessageExecutionContext.getVariables();
		 
		 logMessage(" loResponseMap -> "+loResponseMap);
		 logMessage(" aMessageName -> "+aMessageName);
		 
		 
		 loRuntimeService.createMessageCorrelation(aMessageName).processInstanceBusinessKey(aProcessBusinessKey)
	     .setVariables(loResponseMap).correlateWithResult();
	     //.correlateStartMessage();
		 
		 //ProcessInstance loProcessInstance = runtimeService.startProcessInstanceByMessage(aMessageName, aProcessBusinessKey, loResponseMap);
		 //String lsProcessID = loProcessInstance.getProcessInstanceId();
 		
		 //logMessage("New Process Created with ID -> "+lsProcessID);
		 
		 logMessage(" << delegateAndExecuteMessageWithBusinessKey << ");
	}//delegateAndExecuteMessage
	

	@Override
	public void processMessageWithVariable(String aQueueName, Message aMessage, String aProcessVariableName,
	Object aProcessVariableValue) throws VanquishApplicationException {
		 logMessage(" >> processMessageWithVariable >> ");
		 VanquishConfigManager loVanquishConfigManager = VanquishConfigManager.getInstance();
		 String lsMessageName = loVanquishConfigManager.getMessageNameForQueue(aQueueName);
		 logMessage(" aQueueName            -> "+aQueueName);
		 logMessage(" lsMessageName         -> "+lsMessageName);
		 logMessage(" aProcessVariableName  -> "+aProcessVariableName);
		 logMessage(" aProcessVariableValue -> "+aProcessVariableValue);
		 delegateAndExecuteMessageWithVariable(lsMessageName,aMessage, aProcessVariableName,aProcessVariableValue);
		 logMessage(" << processMessageWithVariable << ");
	}//eof processMessageWithVariable
	
	
	/**
	 * 
	 * @param aMessageName
	 * @param aMessage
	 * @param aProcessVariableName
	 * @param aProcessVariableValue
	 * @throws Exception
	 */
	public void delegateAndExecuteMessageWithVariable(String aMessageName, Message aMessage, String aProcessVariableName,
	Object aProcessVariableValue) throws VanquishApplicationException 
	{
				
			logMessage(" >> delegateAndExecuteMessageWithVariable >> ");
			logMessage(" aMessageName          -> "+aMessageName);
			logMessage(" aProcessVariableName  -> "+aProcessVariableName);
			logMessage(" aProcessVariableValue -> "+aProcessVariableValue);
				
			HashMap<String,Object> loVariablesMap = new HashMap<String,Object>();
				 
			MessageExecutionContext loMessageExecutionContext = new MessageExceutionContextImpl(loVariablesMap, aMessageName, null);
				 
			MessageDelegateProcessor loMessageDelegateProcessor = new VanquishMessageDelegateExecutor();
				 
			loMessageDelegateProcessor.executeMessage(loMessageExecutionContext, aMessage);
				 
			//Post Execution; get the response objects and set it back to business process context for subsequent task
			//to use.
			HashMap<String,Object> loResponseMap = (HashMap<String, Object>) loMessageExecutionContext.getVariables();
				 
			logMessage(" loResponseMap -> "+loResponseMap);
			logMessage(" aMessageName -> "+aMessageName);
				 
			RuntimeService loRuntimeService = processEngine.getRuntimeService();
			logMessage(" loRuntimeService -> "+loRuntimeService);
				 
				 
			loRuntimeService.createMessageCorrelation(aMessageName).processInstanceVariableEquals(aProcessVariableName, aProcessVariableValue)
			        .setVariables(loResponseMap)
			        .correlateWithResult();
				 
			logMessage(" << delegateAndExecuteMessageWithVariable << ");
	}//delegateAndExecuteMessageWithVariable

	@Override
	public void processMessageWithVariableMap(String aQueueName, Message aMessage, Map<String, Object> aProcessVariableMap)
	throws VanquishApplicationException {
		 logMessage(" >> processMessageWithVariableMap >> ");
		 VanquishConfigManager loVanquishConfigManager = VanquishConfigManager.getInstance();
		 String lsMessageName = loVanquishConfigManager.getMessageNameForQueue(aQueueName);
		 logMessage(" aQueueName            -> "+aQueueName);
		 logMessage(" lsMessageName         -> "+lsMessageName);
		 logMessage(" aProcessVariableMap  -> "+aProcessVariableMap);
		 delegateAndExecuteMessageWithVariableMap(lsMessageName,aMessage, aProcessVariableMap);
		 logMessage(" << processMessageWithVariableMap << ");
	}//eof processMessageWithVariableMap
	
    /**
     * 
     * @param aMessageName
     * @param aMessage
     * @param aProcessVariableMap
     * @throws Exception
     */
	public void delegateAndExecuteMessageWithVariableMap(String aMessageName, Message aMessage, Map<String, Object> aProcessVariableMap) 
	throws VanquishApplicationException 
	{
		logMessage(" >> delegateAndExecuteMessageWithVariableMap >> ");
		logMessage(" aMessageName          -> "+aMessageName);
		logMessage(" aProcessVariableMap  -> "+aProcessVariableMap);
						
		HashMap<String,Object> loVariablesMap = new HashMap<String,Object>();
						 
		MessageExecutionContext loMessageExecutionContext = new MessageExceutionContextImpl(loVariablesMap, aMessageName, null);
						 
		MessageDelegateProcessor loMessageDelegateProcessor = new VanquishMessageDelegateExecutor();
						 
		loMessageDelegateProcessor.executeMessage(loMessageExecutionContext, aMessage);
						 
		//Post Execution; get the response objects and set it back to business process context for subsequent task
		//to use.
		HashMap<String,Object> loResponseMap = (HashMap<String, Object>) loMessageExecutionContext.getVariables();
						 
		logMessage(" loResponseMap -> "+loResponseMap);
		logMessage(" aMessageName -> "+aMessageName);
						 
		RuntimeService loRuntimeService = processEngine.getRuntimeService();
		logMessage(" loRuntimeService -> "+loRuntimeService);
						 	 
		loRuntimeService.createMessageCorrelation(aMessageName).processInstanceVariablesEqual(aProcessVariableMap)
		.setVariables(loResponseMap)
		.correlateWithResult();
						 
		logMessage(" << delegateAndExecuteMessageWithVariableMap << ");
	}//delegateAndExecuteMessageWithVariableMap
	


}
