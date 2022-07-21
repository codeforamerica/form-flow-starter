package org.codeforamerica.formflowstarter.app.config;

import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class ScreenNavigationConfiguration {
  private List<NextScreen> nextScreens = Collections.emptyList();
  private String subflow;
}
