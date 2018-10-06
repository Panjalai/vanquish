package com.rbs.vanquish.framework.bpm.pool.common;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VanquishAppStatusProvider {
	
	private volatile AtomicBoolean runningStatus = new AtomicBoolean(false);
	ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public VanquishAppStatusProvider(boolean aStatus) {
		runningStatus.set(aStatus);;
	}
	
	public boolean isRunningStatus() {
		lock.readLock().lock();
		try 
		{
			return runningStatus.get();
	    } 
		finally 
		{
	        lock.readLock().unlock();
	    }
	}//eof isRunningStatus
	
	public void setRunningStatus(boolean runningStatus) {
		lock.writeLock().lock();
		try 
		{
			this.runningStatus.set(runningStatus);
	    } 
		finally 
		{
	        lock.writeLock().unlock();
	    }
	}//eof setRunningStatus

}//eof VanquishApplicationStatusProvider
