package org.codeforamerica.formflowstarter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codeforamerica.formflowstarter.pages.config.ApplicationConfiguration;
import org.codeforamerica.formflowstarter.pages.config.NextPage;
import org.codeforamerica.formflowstarter.pages.config.PageWorkflowConfiguration;
import org.codeforamerica.formflowstarter.pages.data.ApplicationData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PageController {

  private final ApplicationData applicationData;
  private final List<ApplicationConfiguration> applicationConfigurations;

  public PageController(
      List<ApplicationConfiguration> applicationConfigurations,
      ApplicationData applicationData) {
    this.applicationConfigurations = applicationConfigurations;
    this.applicationData = applicationData;
  }

  @GetMapping("/")
  String getRoot() {
    return "index";
  }

  @GetMapping("{flowName}/{pageName}/navigation")
  RedirectView navigation(
      @PathVariable String flowName,
      @PathVariable String pageName,
      @RequestParam(required = false, defaultValue = "0") Integer option
  ) {
    ApplicationConfiguration currentApplicationConfiguration = applicationConfigurations.stream().filter(
        applicationConfiguration -> applicationConfiguration.getFlowName().equals(flowName)
    ).toList().get(0);
    PageWorkflowConfiguration currentPage = currentApplicationConfiguration.getPageWorkflow(pageName);
    if (currentPage == null) {
      return new RedirectView("/error");
    }

    NextPage nextPage = applicationData.getNextPageName(currentPage, option);
    // TODO Use this to set which flow we are in once we have multiple flows
    PageWorkflowConfiguration nextPageWorkflow = currentApplicationConfiguration
        .getPageWorkflow(nextPage.getPageName());

      return new RedirectView(String.format("/%s/%s", flowName, nextPage.getPageName()));
  }

  @GetMapping("{flowName}/{pageName}")
  ModelAndView getPage(
      @PathVariable String pageName,
      @PathVariable String flowName,
      HttpServletResponse response,
      HttpSession httpSession,
      Locale locale
  ) {
    ApplicationConfiguration currentApplicationConfiguration = applicationConfigurations.stream().filter(
        applicationConfiguration -> applicationConfiguration.getFlowName().equals(flowName)
    ).toList().get(0);
    PageWorkflowConfiguration pageWorkflowConfig = currentApplicationConfiguration.getPageWorkflow(pageName);
    if (pageWorkflowConfig == null) {
      return new ModelAndView("redirect:/error");
    }
    Map<String, Object> model = new HashMap<>();
    model.put("pageName", pageName);
    return new ModelAndView(pageName, model);
  }
}
