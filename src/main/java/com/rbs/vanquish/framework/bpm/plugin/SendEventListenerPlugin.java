package com.rbs.vanquish.framework.bpm.plugin;

import java.util.ArrayList;

import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * A {@link ProcessEnginePlugin} just needs to be a spring bean in order
 * to get automatically registered in the Spring Boot environment.
 */
@Component
public class SendEventListenerPlugin extends AbstractProcessEnginePlugin {
	
	private  final Logger LOGGER = LoggerFactory.getLogger(SendEventListenerPlugin.class);

	@Override
	public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
		if (processEngineConfiguration.getCustomPostBPMNParseListeners() == null) {
			processEngineConfiguration.setCustomPostBPMNParseListeners(new ArrayList<BpmnParseListener>());
		}
		processEngineConfiguration.getCustomPostBPMNParseListeners().add(new AddSendEventListenerToBpmnParseListener());

	}
	
	 private  void logMessage(String lsMessage) {
		 LOGGER.info(lsMessage);
	 }

}
