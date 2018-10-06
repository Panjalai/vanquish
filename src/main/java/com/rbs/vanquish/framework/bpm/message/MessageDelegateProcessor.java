package com.rbs.vanquish.framework.bpm.message;

//import javax.jms.Message;
import org.springframework.messaging.Message;

import com.rbs.vanquish.framework.bpm.common.MessageExecutionContext;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;

public interface MessageDelegateProcessor {
	
	public void executeMessage(MessageExecutionContext aMessageExecutionContext, Message aMessage) throws VanquishApplicationException;

}
