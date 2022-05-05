package org.codeforamerica.formflowstarter.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FormInputsConfigurationFactoryConfig {

  @Bean
  public FormInputsConfigurationFactory formInputsConfigurationFactory() {
    return new FormInputsConfigurationFactory();
  }

  @Bean
  public FormInputsConfiguration formInputsConfiguration() {
    return formInputsConfigurationFactory().getObject();
  }
}
