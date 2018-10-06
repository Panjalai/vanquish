package com.rbs.vanquish.framework.bpm.exception;

public class VanquishApplicationException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VanquishApplicationException() {
		super();
	}
	
	public VanquishApplicationException(String aMessage){
		super(aMessage);
	}
	
	public VanquishApplicationException(String aMessage, Throwable aCause){
		super(aMessage, aCause);
	}
	
	public VanquishApplicationException(String aMessage, Throwable aCause, boolean aEnableSuppression, boolean aWritableStackTrace){
		super(aMessage, aCause, aEnableSuppression, aWritableStackTrace);
	}
	
	public VanquishApplicationException(Throwable aCause){
		super(aCause);
	}

}
