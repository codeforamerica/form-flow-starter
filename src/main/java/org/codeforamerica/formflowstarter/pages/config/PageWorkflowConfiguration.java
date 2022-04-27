package org.codeforamerica.formflowstarter.pages.config;

import java.util.LinkedHashMap;
import java.util.List;
import lombok.Data;

@Data
public class PageWorkflowConfiguration {
  private List<NextPage> nextPages;
//  private Condition skipCondition;
//  private List<PageDatasource> datasources = new ArrayList<>();
//  private String groupName;
//  private String appliesToGroup;
//  private String dataMissingRedirect;
//  private String enrichment;
  private String subtleLinkTargetPage;

  public PageWorkflowConfiguration(LinkedHashMap pageWorkflowConfiguration) {
  }

  public Boolean isDirectNavigation() {
    return true;
  }

//  public Subworkflows getSubworkflows(ApplicationData applicationData) {
//    return applicationData.getSubworkflowsForPageDatasources(datasources);
//  }

//  public boolean isInAGroup() {
//    return groupName != null;
//  }
}
