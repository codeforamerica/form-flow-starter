package org.codeforamerica.formflowstarter.app.config;

import java.util.List;
import lombok.Data;

@Data
public class ScreenNavigationConfiguration {
  private List<NextScreen> nextScreens;
  private String subtleLinkTargetPage;

  public Boolean isDirectNavigation() {
    return true;
  }
}
