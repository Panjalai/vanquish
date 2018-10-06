package com.rbs.vanquish.framework.bpm.exception;

public class VanquishRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public VanquishRuntimeException() {
		super();
	}
	
	public VanquishRuntimeException(String aMessage){
		super(aMessage);
	}
	
	public VanquishRuntimeException(String aMessage, Throwable aCause){
		super(aMessage, aCause);
	}
	
	public VanquishRuntimeException(String aMessage, Throwable aCause, boolean aEnableSuppression, boolean aWritableStackTrace){
		super(aMessage, aCause, aEnableSuppression, aWritableStackTrace);
	}
	
	public VanquishRuntimeException(Throwable aCause){
		super(aCause);
	}

}
