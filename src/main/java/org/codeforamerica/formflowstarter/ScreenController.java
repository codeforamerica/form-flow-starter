package org.codeforamerica.formflowstarter;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
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
import org.codeforamerica.formflowstarter.app.data.SubmissionRepositoryService;
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
  private final SubmissionRepositoryService submissionRepositoryService;
  private final ValidationService validationService;

  public ScreenController(
      List<FlowConfiguration> flowConfigurations,
      InputsConfiguration inputsConfiguration,
      SubmissionRepositoryService submissionRepositoryService,
      ConditionHandler conditionHandler,
      ValidationService validationService) {
    this.flowConfigurations = flowConfigurations;
    this.inputsConfiguration = inputsConfiguration;
    this.submissionRepositoryService = submissionRepositoryService;
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
    var submission = getSubmission(httpSession);
    if (currentScreen == null) {
      return new ModelAndView("redirect:/error");
    }

    Map<String, Object> model = new HashMap<>();

    model.put("flow", flow);
    model.put("screen", screen);

    // if there's formDataSubmission
    if (httpSession.getAttribute("formDataSubmission") != null) {
      model.put("submission", submission);
      model.put("inputData", httpSession.getAttribute("formDataSubmission"));
    } else {
      model.put("submission", submission);
      model.put("inputData", submission.getInputData());
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
    var formDataSubmission = removeEmptyValuesAndFlatten(formData);
    var submission = getSubmission(httpSession);

    var errorMessages = validationService.validate(flow, formDataSubmission);
    Map<String, Object> model = new HashMap<>();

    if (errorMessages.size() > 0) {
      httpSession.setAttribute("errorMessages", errorMessages);
      httpSession.setAttribute("formDataSubmission", formDataSubmission);
      return new ModelAndView(String.format("redirect:/%s/%s", flow, screen));
    } else {
      httpSession.removeAttribute("errorMessages");
      httpSession.removeAttribute("formDataSubmission");
    }

    // if there's already a session
    if (submission.getId() != null) {
      Map<String, Object> inputData = submission.getInputData();

      inputData.forEach((key, value) -> {
        formDataSubmission.merge(key, value, (newValue, oldValue) -> newValue);
      });
      submission.setInputData(formDataSubmission);

      submissionRepositoryService.save(submission);
    } else {
      submission.setFlow(flow);
      submission.setInputData(removeEmptyValuesAndFlatten(formData));
      submissionRepositoryService.save(submission);
      httpSession.setAttribute("id", submission.getId());
    }

    return new ModelAndView(String.format("redirect:/%s/%s/navigation", flow, screen));
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
      Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);
      if (submissionOptional.isPresent()) {
        Submission submission = submissionOptional.get();

        var formDataSubmission = removeEmptyValuesAndFlatten(formData);
        Map<String, Object> inputData = submission.getInputData();

        inputData.forEach((key, value) -> {
          formDataSubmission.merge(key, value, (newValue, oldValue) -> newValue);
        });
        submission.setInputData(formDataSubmission);
        submission.setSubmittedAt(Date.from(Instant.now()));
        submissionRepositoryService.save(submission);
      }
    }
    // Fire async events: send email, generate PDF, send to API, etc...
    return new ModelAndView(String.format("redirect:/%s/%s/navigation", flow, screen));
  }

  @NotNull
  private Map<String, Object> removeEmptyValuesAndFlatten(MultiValueMap<String, String> formData) {
    //TODO Check if we are somehow incorrectly handling the empty checkbox scenario
    return formData.entrySet().stream()
        .map(entry -> {
          // An empty checkboxSet has a hidden value of "" which needs to be removed
          if (entry.getKey().contains("[]") && entry.getValue().size() == 1) {
            entry.setValue(new ArrayList<>());
          }
          if (entry.getValue().size() > 1 && entry.getValue().get(0).equals("")) {
            entry.getValue().remove(0);
          }
          return entry;
        })
        // Flatten arrays to be single values if the array contains one item
        .collect(Collectors.toMap(
            Entry::getKey,
            entry -> entry.getValue().size() == 1 && !entry.getKey().contains("[]") ? entry.getValue().get(0) : entry.getValue()
        ));
  }

  @GetMapping("{flow}/{screen}/navigation")
  RedirectView navigation(
      @PathVariable String flow,
      @PathVariable String screen,
      @RequestParam(required = false, defaultValue = "0") Integer option,
      HttpSession httpSession
  ) {
    var currentScreen = getCurrentScreen(flow, screen);
    if (currentScreen == null) {
      return new RedirectView("/error");
    }
    NextScreen nextScreen;
    if (isConditionalNavigation(currentScreen)
        && getConditionalNextScreen(currentScreen, httpSession).size() > 0) {
      nextScreen = getConditionalNextScreen(currentScreen, httpSession).get(0);
    } else {
      // TODO this needs to throw an error if there are more than 1 next screen that don't have a condition or more than one evaluate to true
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

  private List<NextScreen> getConditionalNextScreen(ScreenNavigationConfiguration currentScreen, HttpSession httpSession) {
    var submission = getSubmission(httpSession);
    List<NextScreen> screensWithConditionalNavigation =
        currentScreen.getNextScreens().stream()
            .filter(nextScreen -> nextScreen.getCondition() != null).toList();

    return screensWithConditionalNavigation.stream().filter(nextScreen -> {
      String conditionName = nextScreen.getCondition().getName();
      try {
        conditionHandler.setSubmission(submission);
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

  private Submission getSubmission(HttpSession httpSession) {
    var id = (Long) httpSession.getAttribute("id");
    if (id != null) {
      Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);
      return submissionOptional.orElseGet(Submission::new);
    } else {
      return new Submission();
    }
  }
}
