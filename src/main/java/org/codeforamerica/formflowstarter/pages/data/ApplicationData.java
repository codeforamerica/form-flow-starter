package org.codeforamerica.formflowstarter.pages.data;

import java.io.Serializable;
import lombok.Data;
import org.codeforamerica.formflowstarter.pages.config.NextPage;
import org.codeforamerica.formflowstarter.pages.config.PageWorkflowConfiguration;
import org.jetbrains.annotations.NotNull;

@Data
public class ApplicationData implements Serializable {

  private String id;

  public NextPage getNextPageName(
      @NotNull PageWorkflowConfiguration currentPage,
      Integer option) {
    return currentPage.getNextPages().get(option);
  }
}
