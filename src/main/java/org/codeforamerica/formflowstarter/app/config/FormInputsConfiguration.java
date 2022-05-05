package org.codeforamerica.formflowstarter.app.config;


import java.util.HashMap;
import lombok.Data;

@Data
public class FormInputsConfiguration {

  private HashMap<String, Input> formInputs;

  public Input getInputConfiguration(String inputName) {
    return this.formInputs.get(inputName);
  }
}
