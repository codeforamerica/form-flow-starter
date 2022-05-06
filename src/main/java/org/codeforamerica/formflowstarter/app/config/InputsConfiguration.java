package org.codeforamerica.formflowstarter.app.config;


import java.util.HashMap;
import lombok.Data;

@Data
public class InputsConfiguration {
  private HashMap<String, Input> inputs;
}
