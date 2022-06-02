package org.codeforamerica.formflowstarter;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codeforamerica.formflowstarter.app.config.ConditionHandler;
import org.codeforamerica.formflowstarter.app.config.FlowConfiguration;
import org.codeforamerica.formflowstarter.app.config.InputsConfiguration;
import org.codeforamerica.formflowstarter.app.config.NextScreen;
import org.codeforamerica.formflowstarter.app.config.ScreenNavigationConfiguration;
import org.codeforamerica.formflowstarter.app.data.Submission;
import org.codeforamerica.formflowstarter.app.data.SubmissionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ScreenController {

  private final List<FlowConfiguration> flowConfigurations;
  private final InputsConfiguration inputsConfiguration;
  private final ConditionHandler conditionHandler;
  private final SubmissionService submissionService;
  private final ValidationService validationService;

  public ScreenController(
      List<FlowConfiguration> flowConfigurations,
      InputsConfiguration inputsConfiguration,
      SubmissionService submissionService,
      ConditionHandler conditionHandler,
      ValidationService validationService) {
    this.flowConfigurations = flowConfigurations;
    this.inputsConfiguration = inputsConfiguration;
    this.submissionService = submissionService;
    this.conditionHandler = conditionHandler;
    this.validationService = validationService;
  }

  @GetMapping("{flow}/{screen}")
  ModelAndView getScreen(
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

    model.put("flow", flow);
    model.put("screen", screen);

    // TODO: DRY this up?
    // if there's formDataSubmission
    if (httpSession.getAttribute("formDataSubmission") != null) {
      model.put("inputData", httpSession.getAttribute("formDataSubmission"));
    }
    // if there's no formDataSubmission, but an active session
    else if (httpSession.getAttribute("id") != null) {
      Long id = (Long) httpSession.getAttribute("id");
      Optional<Submission> submissionOptional = submissionService.findById(id);
      if (submissionOptional.isPresent()) {
        Submission submission = submissionOptional.get();
        model.put("submission", submission);
        model.put("inputData", submission.getInputData());
      }
    } else {
      model.put("inputData", new HashMap<>());
    }

    model.put("errorMessages", httpSession.getAttribute("errorMessages"));

    return new ModelAndView("/%s/%s".formatted(flow, screen), model);
  }

  @PostMapping("{flow}/{screen}")
  ModelAndView postScreen(
      @RequestParam(required = false) MultiValueMap<String, String> formData,
      @PathVariable String flow,
      @PathVariable String screen,
      HttpSession httpSession
  ) {
    var formDataSubmission = convertToMultiOrSingleValueMap(formData);

    var errorMessages = validationService.validate(flow, formDataSubmission);
    Map<String, Object> model = new HashMap<>();

    // TODO: on error, redirect to same page with error messages
    if (errorMessages.size() > 0) {
      httpSession.setAttribute("errorMessages", errorMessages);
      System.out.println(httpSession.toString());
      httpSession.setAttribute("formDataSubmission", formDataSubmission);
      return new ModelAndView(String.format("redirect:/%s/%s", flow, screen));
    } else {
      httpSession.removeAttribute("errorMessages");
      httpSession.removeAttribute("formDataSubmission");
    }

    // TODO: DRY this up?
    // if there's already a session
    Long id = (Long) httpSession.getAttribute("id");
    if (id != null) {
      Optional<Submission> submissionOptional = submissionService.findById(id);
      if (submissionOptional.isPresent()) {
        Submission submission = submissionOptional.get();

        Map<String, Object> inputData = submission.getInputData();

        inputData.forEach((key, value) -> {
          formDataSubmission.merge(key, value, (newValue, oldValue) -> newValue);
        });
        submission.setInputData(formDataSubmission);

        submissionService.save(submission);
      }
    } else {
      var submission = new Submission();
      submission.setFlow(flow);
      submission.setInputData(convertToMultiOrSingleValueMap(formData));
      submissionService.save(submission);
      httpSession.setAttribute("id", submission.getId());
    }

    return new ModelAndView(String.format("redirect:%s/navigation", screen));
  }

  @PostMapping("{flow}/{screen}/submit")
  ModelAndView submit(
      @RequestParam(required = false) MultiValueMap<String, String> formData,
      @PathVariable String flow,
      @PathVariable String screen,
      HttpSession httpSession
  ) {
    Long id = (Long) httpSession.getAttribute("id");
    if (id != null) {
      Optional<Submission> submissionOptional = submissionService.findById(id);
      if (submissionOptional.isPresent()) {
        Submission submission = submissionOptional.get();

        var formDataSubmission = convertToMultiOrSingleValueMap(formData);
        Map<String, Object> inputData = submission.getInputData();

        inputData.forEach((key, value) -> {
          formDataSubmission.merge(key, value, (newValue, oldValue) -> newValue);
        });
        submission.setInputData(formDataSubmission);
        submission.setSubmittedAt(Date.from(Instant.now()));
        submissionService.save(submission);
      }
    }
    // Fire async events: send email, generate PDF, send to API, etc...
    return new ModelAndView(String.format("redirect:/%s/%s/navigation", flow, screen));
  }

  @NotNull
  private Map<String, Object> convertToMultiOrSingleValueMap(MultiValueMap<String, String> model) {
    return model.entrySet().stream()
        // Filter out empty value in array with multiple values (empty value was created from hidden input)
        .map(entry -> {
          if (entry.getValue().size() > 1 && entry.getValue().get(0).equals("")) {
            entry.getValue().remove(0);
          }
          return entry;
        })
        .collect(Collectors.toMap(
            Entry::getKey,
            entry -> entry.getValue().size() == 1 ? entry.getValue().get(0) : entry.getValue()
        ));
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
    if (isConditionalNavigation(currentScreen)
        && getConditionalNextScreen(currentScreen).size() > 0) {
      nextScreen = getConditionalNextScreen(currentScreen).get(0);
    } else {
      // TODO this needs to throw an error if there are more than 1 next screen that don't have a condition
      nextScreen = getNonConditionalNextScreen(currentScreen);
    }

    return new RedirectView("/%s/%s".formatted(flow, nextScreen.getName()));
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
