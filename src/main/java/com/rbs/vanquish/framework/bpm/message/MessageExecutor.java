package com.rbs.vanquish.framework.bpm.message;

import com.rbs.vanquish.framework.bpm.common.MessageExecutionContext;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;
//import javax.jms.Message;
import org.springframework.messaging.Message;

public interface MessageExecutor {
	
	public void executeMessage(MessageExecutionContext aMessageExecutionContext, Message aMessage) throws VanquishApplicationException;

}
