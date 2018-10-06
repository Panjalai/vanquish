package com.rbs.vanquish.framework.bpm.message;

//import javax.jms.Message;
import org.springframework.messaging.Message;

import com.rbs.vanquish.framework.bpm.common.MessageExecutionContext;
import com.rbs.vanquish.framework.bpm.config.VanquishConfigManager;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;
import com.rbs.vanquish.framework.bpm.exception.VanquishRuntimeException;

public  class VanquishMessageDelegateExecutor implements MessageDelegateProcessor{

	
	@Override
	public void executeMessage(MessageExecutionContext aMessageExecutionContext, Message aMessage) 
			throws VanquishApplicationException
	{
		
		logMessage("*********** << Inside VanquishMessageExecutor >> ***********");
		String lsMessageName = aMessageExecutionContext.getMessageName();
		logMessage("lsMessageName 	-> "+ lsMessageName);
		try
		{
			MessageExecutor loMessageExecutor = buildMessageExcecutor(lsMessageName);
			loMessageExecutor.executeMessage(aMessageExecutionContext, aMessage);
		}
		catch (VanquishApplicationException aVanquishApplicationException)
		{
			aVanquishApplicationException.printStackTrace();
			throw new RuntimeException (aVanquishApplicationException.getMessage(),aVanquishApplicationException.getCause());
		}
		logMessage("*********** << Outside VanquishTaskExecutor >> ***********");
	}
	
	
	private MessageExecutor buildMessageExcecutor(String aMessageName)
	throws VanquishApplicationException
	{
		logMessage("Inside buildMessageExcecutor >>");
		try
		{
			VanquishConfigManager loVanquishConfigManager = VanquishConfigManager.getInstance();
			
			logMessage("aMessageName 	-> "+ aMessageName);
			String lsClass = loVanquishConfigManager.getJavaClassForEvent(aMessageName);
			
			if (lsClass == null) lsClass = "";
			logMessage("lsClass 	-> "+ lsClass);
			Class<?> loClass = Class.forName(lsClass);
			
			MessageExecutor loMessageExecutor = (MessageExecutor) loClass.newInstance();
			return loMessageExecutor;
		}
		catch(Exception aException)
		{
			aException.printStackTrace();
			throw new VanquishRuntimeException(aException);
		}
	}//eof buildMessageExcecutor
	
	private void logMessage(String lsMessage) {
		//System.out.println(lsMessage);
	}

}
