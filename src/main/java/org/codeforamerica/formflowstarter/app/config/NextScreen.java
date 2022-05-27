package org.codeforamerica.formflowstarter.app.config;

import lombok.Data;

@Data
public class NextScreen {
  private String name;
  private Condition condition;
}
