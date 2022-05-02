package org.codeforamerica.formflowstarter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codeforamerica.formflowstarter.app.config.FlowConfiguration;
import org.codeforamerica.formflowstarter.app.config.NextScreen;
import org.codeforamerica.formflowstarter.app.config.ScreenNavigationConfiguration;
import org.codeforamerica.formflowstarter.app.data.ApplicationData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ScreenController {

  private final ApplicationData applicationData;
  private final List<FlowConfiguration> flowConfigurations;

  public ScreenController(
      List<FlowConfiguration> flowConfigurations,
      ApplicationData applicationData) {
    this.flowConfigurations = flowConfigurations;
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
    FlowConfiguration currentFlowConfiguration = flowConfigurations.stream().filter(
        flowConfiguration -> flowConfiguration.getName().equals(flowName)
    ).toList().get(0);
    ScreenNavigationConfiguration currentPage = currentFlowConfiguration.getScreenNavigation(pageName);
    if (currentPage == null) {
      return new RedirectView("/error");
    }

    NextScreen nextScreen = applicationData.getNextScreenName(currentPage, option);
    // TODO Use this to set which flow we are in once we have multiple flows
    ScreenNavigationConfiguration nextPageWorkflow = currentFlowConfiguration
        .getScreenNavigation(nextScreen.getName());

      return new RedirectView(String.format("/%s/%s", flowName, nextScreen.getName()));
  }

  @GetMapping("{flowName}/{pageName}")
  ModelAndView getPage(
      @PathVariable String pageName,
      @PathVariable String flowName,
      HttpServletResponse response,
      HttpSession httpSession,
      Locale locale
  ) {
    FlowConfiguration currentFlowConfiguration = flowConfigurations.stream().filter(
        flowConfiguration -> flowConfiguration.getName().equals(flowName)
    ).toList().get(0);
    ScreenNavigationConfiguration pageWorkflowConfig = currentFlowConfiguration.getScreenNavigation(pageName);
    if (pageWorkflowConfig == null) {
      return new ModelAndView("redirect:/error");
    }
    Map<String, Object> model = new HashMap<>();
    model.put("pageName", pageName);
    return new ModelAndView(pageName, model);
  }
}
