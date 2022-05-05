package org.codeforamerica.formflowstarter.app.config;

import java.io.IOException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

public class InputsConfigurationFactory implements FactoryBean<InputsConfiguration> {

  @Value("${inputsConfig:inputs-config.yaml}")
  String configPath;

  @Override
  public InputsConfiguration getObject() {
    ClassPathResource classPathResource = new ClassPathResource(configPath);

    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setAllowDuplicateKeys(false);
    loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
    loaderOptions.setAllowRecursiveKeys(true);

    Yaml yaml = new Yaml(new Constructor(InputsConfiguration.class), new Representer(),
        new DumperOptions(), loaderOptions);
    InputsConfiguration inputsConfig = null;
    try {
      inputsConfig = yaml.load(classPathResource.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return inputsConfig;
  }

  @Override
  public Class<?> getObjectType() { return InputsConfiguration.class; }

  @Override
  public boolean isSingleton() { return true; }
}
