package com.rbs.vanquish.bpm.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rbs.vanquish.framework.bpm.common.TaskExecutionContext;
import com.rbs.vanquish.framework.bpm.constant.VanquishConstants;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;
import com.rbs.vanquish.framework.bpm.task.TaskExecutor;

public class PerformDuplicateFileFound implements TaskExecutor {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformDuplicateFileFound.class);
	
	public PerformDuplicateFileFound(){
	}
	
	
	@Override
	public void executeTask(TaskExecutionContext aExecutionContext) throws VanquishApplicationException{
		
		logMessage("Inside com.rbs.vanquish.bpm.task.PerformDuplicateFileCheck!");
		logMessage("TaskID      -> "+aExecutionContext.getTaskID());
		logMessage("Process ID  -> "+aExecutionContext.getProcessInstanceID());
		logMessage("Keys        -> "+aExecutionContext.getKeys());
		logMessage("BusinessKey -> "+aExecutionContext.getProcessBusinessKey());
		
		String lsProcesses = (String) aExecutionContext.getValue(VanquishConstants.PROCESS_COMPLETED);
		lsProcesses = lsProcesses.trim();
		if (lsProcesses.length()==0)
		{
			lsProcesses = lsProcesses+"Duplicate File Found";
		}
		else
		{
			lsProcesses = lsProcesses+", Duplicate File Found";
		}
		aExecutionContext.setVariable(VanquishConstants.PROCESS_COMPLETED, lsProcesses);
		
		logMessage("\n\n IAM GOING TO PERFORM DUPLICATE FILE FOUND HERE \n\n");
		
	}
	
	private void logMessage(String lsMessage) {
		LOGGER.info(lsMessage);
	}

}
