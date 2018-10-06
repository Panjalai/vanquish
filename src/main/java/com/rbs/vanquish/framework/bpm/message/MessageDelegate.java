package com.rbs.vanquish.framework.bpm.message;

import java.util.Map;

//import  javax.jms.Message;
import org.springframework.messaging.Message;

import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;

public interface MessageDelegate {

	void processMessageWithBusinessKey(String aQueueName, Message aMessage, String aProcessBusinessKey) throws VanquishApplicationException;
	void processMessageWithVariable(String aQueueName, Message aMessage, String aProcessVariableName, Object aProcessVariableValue) throws VanquishApplicationException;
	void processMessageWithVariableMap(String aQueueName, Message aMessage, Map<String, Object> aProcessVariableMap) throws VanquishApplicationException;

}