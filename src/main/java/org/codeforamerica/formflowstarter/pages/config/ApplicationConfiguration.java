package org.codeforamerica.formflowstarter.pages.config;

import java.util.Map;
import lombok.Data;

@Data
public class ApplicationConfiguration {

  /**
   * Pages with special functionality.
   */
//  private LandmarkPagesConfiguration landmarkPages;

  /**
   * Used for page navigation.
   */
  private Map<String, Map<String, PageWorkflowConfiguration>> flow;

  /**
   * Used for groups/subworkflows - household, jobs
   */
//  private Map<String, PageGroupConfiguration> pageGroups;

  public PageWorkflowConfiguration getPageWorkflow(String flowName, String pageName) {
    String foo = "foo";
    return this.flow.get(flowName).get(pageName);
  }
}
