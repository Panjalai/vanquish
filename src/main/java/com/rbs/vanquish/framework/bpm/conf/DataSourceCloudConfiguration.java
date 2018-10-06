package com.rbs.vanquish.framework.bpm.conf;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class DataSourceCloudConfiguration {

  private  final Logger LOGGER = LoggerFactory.getLogger(DataSourceCloudConfiguration.class);
	
  @Bean
  public Cloud cloud() {
    return new CloudFactory().getCloud();
  }

  @Bean
  public DataSource dataSource() {
    return cloud().getSingletonServiceConnector(DataSource.class, null);
  }
  
  private  void logMessage(String lsMessage) {
		 LOGGER.info(lsMessage);
	 }
}
