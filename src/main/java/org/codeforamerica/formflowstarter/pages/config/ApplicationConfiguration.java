package org.codeforamerica.formflowstarter.pages.config;

import java.util.Map;
import lombok.Data;

@Data
public class ApplicationConfiguration {

  private String flowName;
  private Map<String, PageWorkflowConfiguration> flow;

  public PageWorkflowConfiguration getPageWorkflow(String pageName) {
    return this.flow.get(pageName);
  }
}
