package com.rbs.vanquish.framework.bpm.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MessageExceutionContextImpl implements MessageExecutionContext {
	
	private Map<String,Object> aVariablesMap = new HashMap<String,Object>();
	private String aMessageName = "";
	private String aBusinessKey = "";
	
	public MessageExceutionContextImpl(Map<String,Object> aVariablesMap, String aMessageName, String aBusinessKey){
		this.aVariablesMap = aVariablesMap;
		this.aMessageName = aMessageName;
		this.aBusinessKey = aBusinessKey;
	}

	@Override
	public void setVariable(String aKey, Object aValue){
		aVariablesMap.put(aKey, aValue);
	}

	@Override
	public Map<String, Object> getVariables() {
		return aVariablesMap;
	}

	@Override
	public Set<String> getKeys() {
		return aVariablesMap.keySet();
	}

	@Override
	public Object getValue(String aKey) {
		return aVariablesMap.get(aKey);
	}

	@Override
	public String getMessageName() {
		return aMessageName;
	}

	@Override
	public String getBusinessKey() {
		return aBusinessKey;
	}


}
