package com.rbs.vanquish.bpm.message.handler;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.rbs.vanquish.framework.bpm.message.MessageDelegate;
import com.rbs.vanquish.framework.bpm.message.MessageDelegateImpl;
import com.rbs.vanquish.framework.bpm.message.VanquishMessageListener;


public class OneMessageListener extends VanquishMessageListener implements MessageListener{
	
	private static final String QUEUE_NAME = "VANQUISH_ENGINE.GENERIC.FILE.INBOUND.QUEUE.DEV";
	
	@Resource
    private PlatformTransactionManager transactionManager;
	
	public OneMessageListener() {
		super();
		setQueueName(QUEUE_NAME);
	}
	
	@Override
	public void onMessage(Message aMessage) {
		processMessage(aMessage);
	}
	
	/**
	 * 
	 * @param aMessage
	 */
	@ Transactional
	private void processMessage(Message aMessage) {
		
		TransactionDefinition loTxnDef = new DefaultTransactionDefinition();
        TransactionStatus loStatus = transactionManager.getTransaction(loTxnDef);
		try 
		{
			MessageDelegate loMessageDelegate = new MessageDelegateImpl();
			org.springframework.messaging.Message loMsg = null;
			String lsBusinessKey = null;
			loMessageDelegate.processMessageWithBusinessKey(getQueueName(), loMsg, lsBusinessKey);
			
			transactionManager.commit(loStatus);
		} 
		catch (Exception aException) 
		{
			aException.printStackTrace();
			transactionManager.rollback(loStatus);
		}
	}//eof processMessage
	
}//eof OneMessageListener
