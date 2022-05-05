package org.codeforamerica.formflowstarter.app.config;

import java.io.IOException;
import java.io.InputStream;
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

public class FormInputsConfigurationFactory implements FactoryBean<FormInputsConfiguration> {

  @Value("${formInputsConfig:form-inputs-config.yaml}")
  String configPath;

  @Override
  public FormInputsConfiguration getObject() {
    ClassPathResource classPathResource = new ClassPathResource(configPath);

    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setAllowDuplicateKeys(false);
    loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
    loaderOptions.setAllowRecursiveKeys(true);

    Yaml yaml = new Yaml(new Constructor(FormInputsConfiguration.class), new Representer(),
        new DumperOptions(), loaderOptions);
    FormInputsConfiguration formInputsConfig = null;
    try {
      formInputsConfig = yaml.load(classPathResource.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return formInputsConfig;
  }

  @Override
  public Class<?> getObjectType() { return FormInputsConfiguration.class; }

  @Override
  public boolean isSingleton() { return true; }
}
