package org.codeforamerica.formflowstarter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codeforamerica.formflowstarter.app.config.FlowConfiguration;
import org.codeforamerica.formflowstarter.app.config.InputsConfiguration;
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
  private final InputsConfiguration inputsConfiguration;

  public ScreenController(
      List<FlowConfiguration> flowConfigurations,
      ApplicationData applicationData,
      InputsConfiguration inputsConfiguration) {
    this.flowConfigurations = flowConfigurations;
    this.applicationData = applicationData;
    this.inputsConfiguration = inputsConfiguration;
  }

  @GetMapping("{flow}/{screen}/navigation")
  RedirectView navigation(
      @PathVariable String flow,
      @PathVariable String screen,
      @RequestParam(required = false, defaultValue = "0") Integer option
  ) {
    var currentScreen = getCurrentScreen(flow, screen);
    if (currentScreen == null) {
      return new RedirectView("/error");
    }

    NextScreen nextScreen = applicationData.getNextScreenName(currentScreen, option);
    return new RedirectView("/%s/%s".formatted(flow, nextScreen.getName()));
  }

  @GetMapping("{flow}/{screen}")
  ModelAndView getPage(
      @PathVariable String flow,
      @PathVariable String screen,
      HttpServletResponse response,
      HttpSession httpSession,
      Locale locale
  ) {
    var currentScreen = getCurrentScreen(flow, screen);
    if (currentScreen == null) {
      return new ModelAndView("redirect:/error");
    }

    Map<String, Object> model = new HashMap<>();
    var inputs= inputsConfiguration;

    model.put("flow", flow);
    model.put("screen", screen);
    model.put("inputs", inputs);
    return new ModelAndView("/%s/%s".formatted(flow, screen), model);
  }

  private ScreenNavigationConfiguration getCurrentScreen(String flow, String screen) {
    FlowConfiguration currentFlowConfiguration = flowConfigurations.stream().filter(
            flowConfiguration -> flowConfiguration.getName().equals(flow)
    ).toList().get(0);
    return currentFlowConfiguration.getScreenNavigation(screen);
  }
}
