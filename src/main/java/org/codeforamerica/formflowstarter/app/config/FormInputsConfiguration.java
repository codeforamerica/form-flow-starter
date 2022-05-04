package org.codeforamerica.formflowstarter.app.config;


import java.util.HashMap;

public class FormInputsConfiguration {

  private HashMap<String, InputConfiguration> inputs;

  public InputConfiguration getInputConfiguration(String inputName) {
    return this.inputs.get(inputName);
  }
}
