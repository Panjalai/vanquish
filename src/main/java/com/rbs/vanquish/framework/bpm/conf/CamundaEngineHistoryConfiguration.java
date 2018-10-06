package com.rbs.vanquish.framework.bpm.conf;


import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaHistoryLevelAutoHandlingConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaEngineHistoryConfiguration extends AbstractCamundaConfiguration implements CamundaHistoryLevelAutoHandlingConfiguration {

  private  final Logger LOGGER = LoggerFactory.getLogger(CamundaIdGeneratorConfiguration.class);
  @Override
  public void preInit(SpringProcessEngineConfiguration configuration) {
    configuration.setHistory(ProcessEngineConfiguration.HISTORY_FULL);
  }
  private  void logMessage(String lsMessage) {
		 LOGGER.info(lsMessage);
	 }
}
