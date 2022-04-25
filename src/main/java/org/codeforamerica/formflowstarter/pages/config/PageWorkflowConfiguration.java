package org.codeforamerica.formflowstarter.pages.config;

import java.util.List;
import lombok.Data;

@Data
public class PageWorkflowConfiguration {

  private List<NextPage> nextPages;
//  private Condition skipCondition;
//  private List<PageDatasource> datasources = new ArrayList<>();
  private PageConfiguration pageConfiguration;
//  private String groupName;
//  private String appliesToGroup;
//  private String dataMissingRedirect;
//  private String enrichment;
  private String subtleLinkTargetPage;

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
