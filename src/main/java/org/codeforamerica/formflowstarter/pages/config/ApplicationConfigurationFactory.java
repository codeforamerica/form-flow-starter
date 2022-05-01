package org.codeforamerica.formflowstarter.pages.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

public class ApplicationConfigurationFactory implements FactoryBean<List<ApplicationConfiguration>> {

  @Value("${pagesConfig:pages-config.yaml}")
  String configPath;

  @Override
  public List<ApplicationConfiguration> getObject() {
    ClassPathResource classPathResource = new ClassPathResource(configPath);

    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setAllowDuplicateKeys(false);
    loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
    loaderOptions.setAllowRecursiveKeys(true);

    Yaml yaml = new Yaml(new Constructor(ApplicationConfiguration.class), new Representer(),
        new DumperOptions(), loaderOptions);
    List<ApplicationConfiguration> appConfigs = new ArrayList<>();
    try {
      Iterable<Object> appConfigsIterable = yaml.loadAll(classPathResource.getInputStream());
      appConfigsIterable.forEach(appConfig -> {
        appConfigs.add((ApplicationConfiguration) appConfig);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    return appConfigs;
  }

  @Override
  public Class<?> getObjectType() {
    return ApplicationConfiguration.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }
}
