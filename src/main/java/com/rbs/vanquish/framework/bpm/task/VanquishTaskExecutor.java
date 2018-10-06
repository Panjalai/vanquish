package com.rbs.vanquish.framework.bpm.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.rbs.vanquish.framework.bpm.common.TaskExecutionContext;
import com.rbs.vanquish.framework.bpm.common.TaskExecutionContextImpl;
import com.rbs.vanquish.framework.bpm.config.VanquishConfigManager;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;


public class VanquishTaskExecutor implements JavaDelegate {

	public void execute(DelegateExecution aDelegateExecution) throws Exception {
		logMessage("*********** << Inside VanquishTaskExecutor >> ***********");
		String lsCurrentActivityID = aDelegateExecution.getCurrentActivityId();
		logMessage("CurrentActivityID 	-> "+lsCurrentActivityID);
		logMessage("EventName 			-> "+aDelegateExecution.getEventName());
		logMessage("ProcessBusinessKeY 	-> "+aDelegateExecution.getProcessBusinessKey());
		logMessage("CurrentActivityName -> "+aDelegateExecution.getCurrentActivityName());
		logMessage("*********** << Inside VanquishTaskExecutor >> ***********");
		try
		{
			TaskExecutionContext loExecutionContext = buildExecutionContext(aDelegateExecution);
			TaskExecutor loTaskExecutor = buildTaskExcecutor(lsCurrentActivityID);
			loTaskExecutor.executeTask(loExecutionContext);
		}
		catch (VanquishApplicationException aVanquishApplicationException)
		{
			aVanquishApplicationException.printStackTrace();
			throw new RuntimeException (aVanquishApplicationException.getMessage(),aVanquishApplicationException.getCause());
		}
	}
	
	private TaskExecutionContext buildExecutionContext (DelegateExecution aDelegateExecution)  throws Exception{
		logMessage("Inside buildExecutionContext >>");
		TaskExecutionContext loExecutionContext = new TaskExecutionContextImpl(aDelegateExecution);
		return loExecutionContext;
	}
	
	private TaskExecutor buildTaskExcecutor(String aTaskActivityName) throws Exception{
		logMessage("Inside buildTaskExcecutor >>");
		VanquishConfigManager loVanquishConfigManager = VanquishConfigManager.getInstance();
		String lsClass = loVanquishConfigManager.getJavaClassForTask(aTaskActivityName);
		if (lsClass == null) lsClass = "";
		Class<?> loClass = Class.forName(lsClass);
		TaskExecutor loTaskExecutor = (TaskExecutor) loClass.newInstance();
		return loTaskExecutor;
	}
	
	private void logMessage(String lsMessage) {
		//System.out.println(lsMessage);
	}

}
