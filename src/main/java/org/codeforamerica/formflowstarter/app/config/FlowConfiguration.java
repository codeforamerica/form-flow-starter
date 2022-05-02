package org.codeforamerica.formflowstarter.app.config;

import java.util.Map;
import lombok.Data;

@Data
public class FlowConfiguration {

  private String name;
  private Map<String, ScreenNavigationConfiguration> flow;

  public ScreenNavigationConfiguration getScreenNavigation(String screenName) {
    return this.flow.get(screenName);
  }
}
