package com.rbs.vanquish.bpm.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rbs.vanquish.framework.bpm.common.TaskExecutionContext;
import com.rbs.vanquish.framework.bpm.constant.VanquishConstants;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;
import com.rbs.vanquish.framework.bpm.task.TaskExecutor;

public class PerformDuplicateFileCheck implements TaskExecutor{

	private static final Logger LOGGER = LoggerFactory.getLogger(PerformDuplicateFileCheck.class);
	public PerformDuplicateFileCheck(){
		
	}
	
	@Override
	public void executeTask(TaskExecutionContext aExecutionContext) throws VanquishApplicationException {
		
		logMessage("Inside com.rbs.vanquish.bpm.task.PerformDuplicateFileCheck!");
		logMessage("TaskID      -> "+aExecutionContext.getTaskID());
		logMessage("Process ID  -> "+aExecutionContext.getProcessInstanceID());
		logMessage("Keys        -> "+aExecutionContext.getKeys());
		logMessage("BusinessKey -> "+aExecutionContext.getProcessBusinessKey());
		
		String lsProcesses = (String) aExecutionContext.getValue(VanquishConstants.PROCESS_COMPLETED);
		lsProcesses = lsProcesses.trim();
		if (lsProcesses.length()==0)
		{
			lsProcesses = lsProcesses+"Duplicate Check";
		}
		else
		{
			lsProcesses = lsProcesses+", Duplicate Check";
		}
		aExecutionContext.setVariable(VanquishConstants.PROCESS_COMPLETED, lsProcesses);
		
		logMessage("\n\n IAM GOING TO PERFORM DUPLICATE FILE CHECK HERE \n\n");
		
		//aExecutionContext.setVariable("duplicate", true);
		
	}
	
	private void logMessage(String lsMessage) {
		LOGGER.info(lsMessage);
	}

}
