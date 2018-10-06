package com.rbs.vanquish.bpm.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rbs.vanquish.framework.bpm.common.TaskExecutionContext;
import com.rbs.vanquish.framework.bpm.constant.VanquishConstants;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;
import com.rbs.vanquish.framework.bpm.task.TaskExecutor;

public class PerformInputFileTransformed implements TaskExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PerformInputFileTransformed.class);
	public PerformInputFileTransformed(){
	}
	
	
	@Override
	public void executeTask(TaskExecutionContext aExecutionContext) throws VanquishApplicationException {
		
		logMessage("Inside com.rbs.vanquish.bpm.task.PerformInputFileTransformed!");
		logMessage("TaskID      -> "+aExecutionContext.getTaskID());
		logMessage("Process ID  -> "+aExecutionContext.getProcessInstanceID());
		logMessage("Keys        -> "+aExecutionContext.getKeys());
		logMessage("BusinessKey -> "+aExecutionContext.getProcessBusinessKey());
		
		String lsBusinessKey = (String) aExecutionContext.getValue(VanquishConstants.UNIQUE_REFERENCE_KEY);
		
		String lsProcesses = (String) aExecutionContext.getValue(VanquishConstants.PROCESS_COMPLETED);
		lsProcesses = lsProcesses.trim();
		if (lsProcesses.length()==0)
		{
			lsProcesses = lsProcesses+"Input File Transformed";
		}
		else
		{
			lsProcesses = lsProcesses+", Input File Transformed";
		}
		aExecutionContext.setVariable(VanquishConstants.PROCESS_COMPLETED, lsProcesses);
		
		logMessage("\n\n\n\n");
		logMessage("--------------------------------------------------------------------------------------------------");
		logMessage("   BUSINESS KEY : "+lsBusinessKey+" COMPLETED");
		logMessage("--------------------------------------------------------------------------------------------------");
		logMessage("\n\n\n\n");
		
		
		
	}
	
	private void logMessage(String lsMessage) {
		LOGGER.info(lsMessage);
	}

}
