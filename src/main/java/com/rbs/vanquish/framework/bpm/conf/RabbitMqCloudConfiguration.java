package com.rbs.vanquish.framework.bpm.conf;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("cloud")
public class RabbitMqCloudConfiguration extends AbstractCloudConfig {
  private  final Logger LOGGER = LoggerFactory.getLogger(RabbitMqCloudConfiguration.class);

  @Bean
  public ConnectionFactory rabbitConnectionFactory() {
    return connectionFactory().rabbitConnectionFactory();
  }
  
  private  void logMessage(String lsMessage) {
		 LOGGER.info(lsMessage);
	 }
}
