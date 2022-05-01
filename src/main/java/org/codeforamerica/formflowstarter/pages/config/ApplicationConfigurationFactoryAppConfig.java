package org.codeforamerica.formflowstarter.pages.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfigurationFactoryAppConfig {

  @Bean
  public ApplicationConfigurationFactory applicationConfigurationFactory() {
    return new ApplicationConfigurationFactory();
  }

  @Bean
  public List<ApplicationConfiguration> applicationConfiguration() throws Exception {
    return applicationConfigurationFactory().getObject();
  }

}
