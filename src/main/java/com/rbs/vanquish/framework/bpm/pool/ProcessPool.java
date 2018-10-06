package com.rbs.vanquish.framework.bpm.pool;

import java.lang.Thread.State;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.rbs.vanquish.framework.bpm.exception.VanquishApplicationException;
import com.rbs.vanquish.framework.bpm.exception.VanquishRuntimeException;
import com.rbs.vanquish.framework.bpm.pool.common.Poolable;
import com.rbs.vanquish.framework.bpm.pool.common.VanquishAppStatusProvider;

public class ProcessPool {
	
	private static int poolSize = 0;
	private static int DEFAULT_POOL_SIZE = 20;
	private static final DateTimeFormatter loDateTimeFormatter = DateTimeFormatter.ofPattern("[dd:MM:yyyy:HH:mm:ss:SSSS]:");
	private static ProcessPool processPoolinstance = null;
	private static Map<String, BPMProcess> processPoolMap = new HashMap<String,BPMProcess>();
	private static String instanceName = "";
	private static int defaultSleepDelay = 300;
	private static VanquishAppStatusProvider vanquishAppStatusProvider= null;
	
	ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private ProcessPool(VanquishAppStatusProvider aVanquishAppStatusProvider, String aInstanceName, int aPoolSize) {
		vanquishAppStatusProvider = aVanquishAppStatusProvider;
		instanceName = aInstanceName;
		poolSize = aPoolSize;
	}
	
	
	public static String getInstanceName() {
		return instanceName;
	}

	public static void setInstanceName(String instanceName) {
		ProcessPool.instanceName = instanceName;
	}

	
	public int getPoolSize() {
		return poolSize;
	}
	
	public void setPoolSize(int poolSize) {
		ProcessPool.poolSize = poolSize;
	}
	
	public static ProcessPool getInstance(VanquishAppStatusProvider aVanquishAppStatusProvider, String aInstanceName, int aPoolSize) throws Exception{
	      if(processPoolinstance == null) 
	      {
	    	  int defaultPoolSize = DEFAULT_POOL_SIZE;
	    	  if (aPoolSize > 0) defaultPoolSize = aPoolSize;
	    	  processPoolinstance = new ProcessPool(aVanquishAppStatusProvider,aInstanceName, defaultPoolSize);
	      }
	      return processPoolinstance;
	}//eof VanquishConfigManager
	
	/**
	 * 
	 * @param aUniqueThreadID
	 * @return
	 * @throws VanquishApplicationException
	 */
	public String getProcessStatus(String aUniqueThreadID) throws VanquishApplicationException {
		logMessage ( "  >> getProcessStatus ()" );
		lock.readLock().lock();
		String lsThreadStatus = "";
		try 
		{
			if (!processPoolMap.keySet().contains(aUniqueThreadID)) {
				throw new VanquishApplicationException("No such thread exists in the pool!");
			}
			
			BPMProcess loBPMProcess = processPoolMap.get(aUniqueThreadID);
			Thread loThread = (Thread) loBPMProcess;
			State loState = loThread.getState();
			lsThreadStatus = loState.toString();
	    } 
		finally 
		{
	        lock.readLock().unlock();
	    }
		logMessage ( "  << getProcessStatus ()" );
		return lsThreadStatus;
	}//eof getProcessStatus
	
	/**
	 * 
	 */
	public void performCleanShutdown() {
		logMessage ( "  >> performCleanShutdown ()" );
		lock.writeLock().lock();
		try 
		{
			vanquishAppStatusProvider.setRunningStatus(false);
			int loCurrentSize = processPoolMap.keySet().size();
			//wait till pool size become zero and all process are
			//successfully completed
			while (loCurrentSize <= 0)
			{
					logMessage ( " << Waiting for the pool to become empty >>" );
					Thread.sleep(defaultSleepDelay);
					refreshLatestPoolFromDatabase();
					loCurrentSize = processPoolMap.keySet().size();
			}//eof while
	    } 
		catch (InterruptedException aInterruptedException) 
		{
			aInterruptedException.printStackTrace();
			throw new VanquishRuntimeException(aInterruptedException);
		} 
		finally 
		{
	        lock.writeLock().unlock();
	    }
		logMessage ( "  << performCleanShutdown ()" );
	}//eof performCleanShutdown
	
	/**
	 * 
	 * @param aBPMProcess
	 * @throws VanquishApplicationException
	 */
	public void startTheProcessFromPool(BPMProcess aBPMProcess) throws VanquishApplicationException {
		logMessage ( "  >> startTheProcessFromPool()" );
		lock.writeLock().lock();
		String lsUnquieThreadID = aBPMProcess.getUniqueThreadID();
		try 
		{
				if (!processPoolMap.keySet().contains(lsUnquieThreadID)) {
					throw new VanquishApplicationException("Process doesn't exists! "
					+ "please check the process was added to the pool or not!");
				}
				Thread loThread = (Thread) aBPMProcess;
				loThread.start();
				logMessage ( " Thread Started with UID -> " + aBPMProcess.getUniqueThreadID());
	    } 
		catch (Exception aException) 
		{
			aException.printStackTrace();
			throw new VanquishRuntimeException(aException);
		}
		finally 
		{
	        lock.writeLock().unlock();
	    }
		logMessage ( "  >> startTheProcessFromPool()" );
	}//eof startTheProcessFromPool
	
	/**
	 * 
	 * @param aBPMProcess
	 * @param aBusinessKey
	 * @throws VanquishApplicationException 
	 */
	public String addToProcessPool(BPMProcess aBPMProcess, String aBusinessKey) 
	throws VanquishApplicationException 
	{
		if (!vanquishAppStatusProvider.isRunningStatus()) throw new 
		VanquishRuntimeException("Application shutting down can't add more process to the Pool");
		
		logMessage ( "  >> addToProcessPool()" );
		lock.writeLock().lock();
		try 
		{
			refreshLatestPoolFromDatabase();
			int loCurrentSize = processPoolMap.keySet().size();
			if (loCurrentSize < poolSize)
			{
				insertIntoPool(aBPMProcess,aBusinessKey);
			}//eof provision exists.
			else
			{
				//wait till pool size is free and add to the pool
				while (loCurrentSize >= poolSize)
				{
					logMessage ( " << Waiting for the pool to become free >>" );
					Thread.sleep(defaultSleepDelay);
					refreshLatestPoolFromDatabase();
					loCurrentSize = processPoolMap.keySet().size();
				}//eof while
				
				insertIntoPool(aBPMProcess,aBusinessKey);
			}
	    } 
		catch (InterruptedException aInterruptedException) 
		{
	    	aInterruptedException.printStackTrace();
	    	refreshLatestPoolFromDatabase();
		} 
		finally 
		{
	        lock.writeLock().unlock();
	    }
		logMessage ( "  << addToProcessPool()" );
		return aBPMProcess.getUniqueThreadID();
	}//eof addToProcessPool
	
	/**
	 * 
	 * @param aBPMProcess
	 * @param aBusinessKey
	 * @throws VanquishApplicationException
	 */
	private void insertIntoPool(BPMProcess aBPMProcess, String aBusinessKey) 
	throws VanquishApplicationException 
	{
		logMessage ( "  >> insertIntoPool()" );
		String lsUnquieThreadID = prefixCurrentDateAndTimeStampToTheKey(aBusinessKey);
		if (processPoolMap.keySet().contains(lsUnquieThreadID)) {
			throw new VanquishApplicationException("BusinessKey Already Exists and thread is not unique!");
		}
		Poolable loPoolable = (Poolable) aBPMProcess;
		loPoolable.setUniqueThreadID(lsUnquieThreadID);
		processPoolMap.put(lsUnquieThreadID, aBPMProcess);
		logMessage ( "  << insertIntoPool()" );
	}//eof insertIntoPool
	
	
	//update Map with latest threads from DB use the instance name to know the running processes inside this node
	//clean up all threads which are switched over to other nodes.
	//Each row will have INSTANCEID, BUSINESSKEY, THREADID during the lifecycle of process exceution.
	//A process can switch from one INSTANCEID to other; so we need to refresh the pool from database frequently.
	/**
	 * 
	 */
	private void refreshLatestPoolFromDatabase() {
		logMessage ( "  >> refreshLatestPoolFromDatabase()" );
		logMessage ( "Before Refresh keys -> "+ processPoolMap.keySet());
		
		
		logMessage ( "After Refresh keys  -> "+ processPoolMap.keySet());
		logMessage ( "  << refreshLatestPoolFromDatabase()" );
	}//eof refreshLatestPoolFromDatabase
	
	/**
	 * 
	 * @param aBusinessKey
	 * @return This will result into 30 + length of Business Key String as a thread key
	 */
	private String prefixCurrentDateAndTimeStampToTheKey(String aBusinessKey) {
		String newBusinessKey = aBusinessKey;
		if (newBusinessKey != null) newBusinessKey = newBusinessKey.trim();
		String lsBusinessKey = "["+aBusinessKey+"]";
		LocalDateTime loNow = LocalDateTime.now();
        String lsNewPrefix = loDateTimeFormatter.format(loNow);
        lsBusinessKey = lsNewPrefix + lsBusinessKey;
		return lsBusinessKey;
	}//eof generateCurrentDateAndTimeStamp
	
	private void logMessage(String aMessage) {
		System.out.println(aMessage);
	}
	
	

}
