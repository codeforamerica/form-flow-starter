package org.codeforamerica.formflowstarter.app.config;

import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class InputConfiguration {
  private final String inputName;
  private final InputType type;
  List<Validator> validators = Collections.emptyList();
}
