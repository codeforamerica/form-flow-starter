package org.codeforamerica.formflowstarter.pages.config;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ApplicationConfiguration {

  /**
   * List of all defined pages.
   */
  private List<PageConfiguration> pageDefinitions;

  /**
   * Pages with special functionality.
   */
//  private LandmarkPagesConfiguration landmarkPages;

  /**
   * Used for page navigation.
   */
  private Map<String, PageWorkflowConfiguration> workflow;

  /**
   * Used for groups/subworkflows - household, jobs
   */
//  private Map<String, PageGroupConfiguration> pageGroups;

  public PageWorkflowConfiguration getPageWorkflow(String pageName) {
    return this.workflow.get(pageName);
  }
}
