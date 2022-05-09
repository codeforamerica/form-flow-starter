package org.codeforamerica.formflowstarter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codeforamerica.formflowstarter.app.config.ConditionHandler;
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
  private final ConditionHandler conditionHandler;

  public ScreenController(
      List<FlowConfiguration> flowConfigurations,
      ApplicationData applicationData,
      InputsConfiguration inputsConfiguration,
      ConditionHandler conditionHandler) {
    this.flowConfigurations = flowConfigurations;
    this.applicationData = applicationData;
    this.inputsConfiguration = inputsConfiguration;
    this.conditionHandler = conditionHandler;
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
    NextScreen nextScreen;
    if (isConditionalNavigation(currentScreen) && getConditionalNextScreen(currentScreen).size() > 0) {
      nextScreen = getConditionalNextScreen(currentScreen).get(0);
    } else {
      // TODO this needs to throw an error if there are more than 1 next screen that don't have a condition
      nextScreen = getNonConditionalNextScreen(currentScreen);
    }

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
    var inputs= inputsConfiguration.getInputs();

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

  private Boolean isConditionalNavigation(ScreenNavigationConfiguration currentScreen) {
    return currentScreen.getNextScreens().stream()
        .anyMatch(nextScreen -> nextScreen.getCondition() != null);
  }

  private List<NextScreen> getConditionalNextScreen(ScreenNavigationConfiguration currentScreen) {
    List<NextScreen> screensWithConditionalNavigation =
        currentScreen.getNextScreens().stream()
            .filter(nextScreen -> nextScreen.getCondition() != null).toList();

    return screensWithConditionalNavigation.stream().filter(nextScreen -> {
      String conditionName = nextScreen.getCondition().getName();
      try {
        return conditionHandler.handleCondition(conditionName).equals(true);
      } catch (NoSuchMethodException | InvocationTargetException e) {
        System.out.println("No such method could be found in the ConditionDefinitions class.");
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      return false;
    }).toList();
  }

  private NextScreen getNonConditionalNextScreen(ScreenNavigationConfiguration currentScreen) {
    return currentScreen.getNextScreens().stream()
        .filter(nxtScreen -> nxtScreen.getCondition() == null).toList().get(0);
  }
}
