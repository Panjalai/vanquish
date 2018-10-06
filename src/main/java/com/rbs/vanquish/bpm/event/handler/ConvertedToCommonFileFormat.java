package com.rbs.vanquish.bpm.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import javax.jms.Message;
import org.springframework.messaging.Message;

import com.rbs.vanquish.framework.bpm.common.MessageExecutionContext;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;
import com.rbs.vanquish.framework.bpm.message.MessageExecutor;

public class ConvertedToCommonFileFormat implements MessageExecutor{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConvertedToCommonFileFormat.class);

	@Override
	public void executeMessage(MessageExecutionContext aMessageExecutionContext, Message aMessage)
	throws VanquishApplicationException 
	{
		logMessage("Inside com.rbs.vanquish.bpm.message.ConvertedToCommonFileFormat!");
		
		logMessage("MessageName      -> "+aMessageExecutionContext.getMessageName());
		logMessage("BusinessKey      -> "+aMessageExecutionContext.getBusinessKey());
		aMessageExecutionContext.setVariable("NewKeyInserted", "NewValueInserted");
		
		logMessage("\n\n IAM GOING TO PROCESS MESSAGE FROM  ConvertedToCommonFileFormat \n\n");
	}//eof executeMessage
	
	private void logMessage(String lsMessage) {
		LOGGER.info(lsMessage);
	}

}
