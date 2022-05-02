package org.codeforamerica.formflowstarter.app.data;

import java.io.Serializable;
import lombok.Data;
import org.codeforamerica.formflowstarter.app.config.NextScreen;
import org.codeforamerica.formflowstarter.app.config.ScreenNavigationConfiguration;
import org.jetbrains.annotations.NotNull;

@Data
public class ApplicationData implements Serializable {

  private String id;

  public NextScreen getNextScreenName(
      @NotNull ScreenNavigationConfiguration currentPage,
      Integer option) {
    return currentPage.getNextScreens().get(option);
  }
}
