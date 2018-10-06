package com.rbs.vanquish.framework.bpm.common;

import java.util.Map;
import java.util.Set;

public interface MessageExecutionContext {
	public void setVariable(String aKey, Object aValue);
	public Map<String,Object> getVariables();
	public Set<String> getKeys();
	public Object getValue(String aKey);
	public String getMessageName();
	public String getBusinessKey();
}
