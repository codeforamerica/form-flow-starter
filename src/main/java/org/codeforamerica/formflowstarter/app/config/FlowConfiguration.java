package org.codeforamerica.formflowstarter.app.config;

import java.util.HashMap;

import lombok.Data;

@Data
public class FlowConfiguration {

  private String name;
  private HashMap<String, ScreenNavigationConfiguration> flow;

  private HashMap<String, SubflowConfiguration> subflows;

  public ScreenNavigationConfiguration getScreenNavigation(String screenName) {
    return this.flow.get(screenName);
  }
}
