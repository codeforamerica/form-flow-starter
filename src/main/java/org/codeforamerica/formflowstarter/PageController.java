package org.codeforamerica.formflowstarter;

import java.util.HashMap;
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
  private final ApplicationConfiguration applicationConfiguration;

  public PageController(
      ApplicationConfiguration applicationConfiguration,
      ApplicationData applicationData) {
    this.applicationConfiguration = applicationConfiguration;
    this.applicationData = applicationData;
  }

  @GetMapping("/page")
  String getPage() {
    return "page";
  }

  @GetMapping("/")
  String getRoot() {
    return "index";
  }

  @GetMapping("/pages/{pageName}/navigation")
  RedirectView navigation(
      @PathVariable String pageName,
      @RequestParam(required = false, defaultValue = "0") Integer option
  ) {
    PageWorkflowConfiguration currentPage = applicationConfiguration.getPageWorkflow(pageName);
    if (currentPage == null) {
      return new RedirectView("/error");
    }

    NextPage nextPage = applicationData.getNextPageName(currentPage, option);
    // TODO Use this to set which flow we are in once we have multiple flows
    PageWorkflowConfiguration nextPageWorkflow = applicationConfiguration
        .getPageWorkflow(nextPage.getPageName());

      return new RedirectView(String.format("/pages/%s", nextPage.getPageName()));
  }

  @GetMapping("/pages/{pageName}")
  ModelAndView getPage(
      @PathVariable String pageName,
      HttpServletResponse response,
      HttpSession httpSession,
      Locale locale
  ) {

    PageWorkflowConfiguration pageWorkflowConfig = applicationConfiguration.getPageWorkflow(pageName);
    if (pageWorkflowConfig == null) {
      return new ModelAndView("redirect:/error");
    }
    Map<String, Object> model = new HashMap<>();
    model.put("pageName", pageName);
    return new ModelAndView(pageName, model);
  }
}
