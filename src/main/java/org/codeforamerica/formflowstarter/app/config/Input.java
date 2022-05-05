package org.codeforamerica.formflowstarter.app.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Input {
  private String name;
  private InputType type;
  private List<Option> options = new ArrayList<>();
}
