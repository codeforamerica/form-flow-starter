package org.codeforamerica.formflowstarter.app.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Input {
  private final String name;
  private final InputType type;
  private final List<Option> options = new ArrayList<>();
}
