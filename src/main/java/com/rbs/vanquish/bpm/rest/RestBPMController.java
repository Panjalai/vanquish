package com.rbs.vanquish.bpm.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class RestBPMController {
	private  final Logger LOGGER = LoggerFactory.getLogger(RestBPMController.class);
	
	@Autowired
	private RuntimeService runtimeService;

	@RequestMapping(value = "/invokePaymentProcess", method = RequestMethod.GET, produces = "application/json")
	public String getCustomerInfoXML() {
		
		Map<String,Object> loVariables = new HashMap<String,Object>();
		
		Random loRandom = new Random();
		int liRandomVal = loRandom.nextInt(7000);
	    loVariables.put("Key 1", "1234");
	    loVariables.put("Key 2", "xdfrg");
	    loVariables.put("Key 3", liRandomVal);
	    loVariables.put("duplicate", false);

	    
	    ProcessInstance loProcessInstance =  runtimeService.startProcessInstanceByKey("payment_processing_workflow", loVariables);
	    String lsProcessID = loProcessInstance.getProcessInstanceId();
	    //String lsProcessID = "Test";
	    		
	    logMessage("New Process Created with ID -> "+lsProcessID);
	    String lsMessage = "Process Created with ID -> "+lsProcessID;
		return lsMessage;
	}//eof getCustomerInfoXML
	
	 private  void logMessage(String lsMessage) {
		 LOGGER.info(lsMessage);
	 }

}
