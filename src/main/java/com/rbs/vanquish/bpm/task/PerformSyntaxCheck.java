package com.rbs.vanquish.bpm.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rbs.vanquish.framework.bpm.common.TaskExecutionContext;
import com.rbs.vanquish.framework.bpm.constant.VanquishConstants;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;
import com.rbs.vanquish.framework.bpm.task.TaskExecutor;

public class PerformSyntaxCheck implements TaskExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformSyntaxCheck.class);
	public PerformSyntaxCheck(){
	}
	
	
	@Override
	public void executeTask(TaskExecutionContext aExecutionContext) throws VanquishApplicationException {
		
		logMessage("Inside com.rbs.vanquish.bpm.task.PerformSyntaxCheck!");
		logMessage("TaskID      -> "+aExecutionContext.getTaskID());
		logMessage("Process ID  -> "+aExecutionContext.getProcessInstanceID());
		logMessage("Keys        -> "+aExecutionContext.getKeys());
		logMessage("BusinessKey -> "+aExecutionContext.getProcessBusinessKey());
		logMessage("\n\n IAM GOING TO PERFORM SYNTAX CHECK HERE \n\n");
		
		String lsProcesses = (String) aExecutionContext.getValue(VanquishConstants.PROCESS_COMPLETED);
		lsProcesses = lsProcesses.trim();
		if (lsProcesses.length()==0)
		{
			lsProcesses = lsProcesses+"Syntax Check";
		}
		else
		{
			lsProcesses = lsProcesses+", Syntax Check";
		}
		aExecutionContext.setVariable(VanquishConstants.PROCESS_COMPLETED, lsProcesses);
		
	}
	
	private void logMessage(String lsMessage) {
		System.out.println(lsMessage);
	}
}
