package org.codeforamerica.formflowstarter.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputsConfigurationFactoryConfig {

  @Bean
  public InputsConfigurationFactory inputsConfigurationFactory() {
    return new InputsConfigurationFactory();
  }

  @Bean
  public InputsConfiguration inputsConfiguration() {
    return inputsConfigurationFactory().getObject();
  }
}
