package org.codeforamerica.formflowstarter.app.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowsConfigurationFactoryConfig {

  @Bean
  public FlowsConfigurationFactory flowsConfigurationFactory() {
    return new FlowsConfigurationFactory();
  }

  @Bean
  public List<FlowConfiguration> flowsConfiguration() {
    return flowsConfigurationFactory().getObject();
  }
}
