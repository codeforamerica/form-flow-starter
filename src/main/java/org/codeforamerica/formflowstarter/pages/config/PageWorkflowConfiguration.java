package org.codeforamerica.formflowstarter.pages.config;

import java.util.List;
import lombok.Data;

@Data
public class PageWorkflowConfiguration {
  private List<NextPage> nextPages;
  private String subtleLinkTargetPage;

  public Boolean isDirectNavigation() {
    return true;
  }
}
