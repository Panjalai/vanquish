package com.rbs.vanquish.framework.bpm.task;

import com.rbs.vanquish.framework.bpm.common.TaskExecutionContext;
import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;

public interface TaskExecutor {
	
	public void executeTask(TaskExecutionContext aExecutionContext) throws VanquishApplicationException;

}
