package org.codeforamerica.formflowstarter.app.config;


import java.util.HashMap;
import lombok.Data;

@Data
public class InputsConfiguration {

  private HashMap<String, Input> inputs;

  public Input getInputConfiguration(String inputName) {
    return this.inputs.get(inputName);
  }
}
