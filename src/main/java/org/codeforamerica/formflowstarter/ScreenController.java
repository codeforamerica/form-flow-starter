package org.codeforamerica.formflowstarter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codeforamerica.formflowstarter.app.config.FlowConfiguration;
import org.codeforamerica.formflowstarter.app.config.InputsConfiguration;
import org.codeforamerica.formflowstarter.app.config.NextScreen;
import org.codeforamerica.formflowstarter.app.config.ScreenNavigationConfiguration;
import org.codeforamerica.formflowstarter.app.data.ApplicationData;
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
  private final ApplicationData applicationData;
  private final InputsConfiguration inputsConfiguration;
  private final SubmissionService submissionService;


  public ScreenController(
      List<FlowConfiguration> flowConfigurations,
      ApplicationData applicationData,
      InputsConfiguration inputsConfiguration,
      SubmissionService submissionService) {
    this.flowConfigurations = flowConfigurations;
    this.applicationData = applicationData;
    this.inputsConfiguration = inputsConfiguration;
    this.submissionService = submissionService;
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
    var inputs= inputsConfiguration.getInputs();

    model.put("flow", flow);
    model.put("screen", screen);
    model.put("inputs", inputs);
    return new ModelAndView("/%s/%s".formatted(flow, screen), model);
  }

  @PostMapping("{flow}/{screen}")
  ModelAndView postScreen(
      @RequestParam(required = false) MultiValueMap<String, String> formData,
      @PathVariable String flow,
      @PathVariable String screen,
      HttpSession httpSession
  ) {
    // get screen (configuration)
    var currentScreen = getCurrentScreen(flow, screen);

    // if there's already a session
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
        submissionService.save(submission);
        httpSession.setAttribute("inputData", formDataSubmission);
      }
    } else {
      var submission = new Submission();
      submission.setFlow(flow);
      submission.setInputData(convertToMultiOrSingleValueMap(formData));
      submissionService.save(submission);
      httpSession.setAttribute("inputData", convertToMultiOrSingleValueMap(formData));
      httpSession.setAttribute("id", submission.getId());
    }



//    // persist model to database
//    PageData pageData = PageData.fillOut(page, model);
//
//    // read all input data from in-memory model
//    PagesData pagesData;
//    pagesData = applicationData.getPagesData();
//
//    // put input data from this screen to the in-memory model (pagesData)
//    pagesData.putPage(page.getName(), pageData);
//
//    // validate
//    Boolean pageDataIsValid = pageData.isValid();
//    if (pageDataIsValid) {
//      // if it hasn't been saved to that database yet
//      if (applicationData.getId() == null) {
//        applicationData.setId(applicationRepository.getNextId());
//      }
//
//      // set the last page viewed
//      if (pageName != null && !pageName.isEmpty()) {
//        applicationData.setLastPageViewed(pageName);
//      }
//
//      // process any enrichments (before_save)
//      ofNullable(pageWorkflow.getEnrichment())
//          .map(applicationEnrichment::getEnrichment)
//          .map(enrichment -> enrichment.process(pagesData))
//          .ifPresent(pageData::putAll);
//
//      // save to the database
//    Submission submission = applicationFactory.newApplication(applicationData);
//
//      // submissionRepository.beforeSave()
//
//      applicationRepository.save(application);
//
//      // submissionRepository.afterSave()
//
//      // redirect to next screen
//      return new ModelAndView(String.format("redirect:/pages/%s/navigation", pageName));
//    } else {
//      // failed validation, show same screen
//      return new ModelAndView("redirect:/pages/" + pageName);
//    }

    return new ModelAndView(String.format("redirect:%s/navigation", screen));
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

    NextScreen nextScreen = applicationData.getNextScreenName(currentScreen, option);
    return new RedirectView("/%s/%s".formatted(flow, nextScreen.getName()));
  }



  private ScreenNavigationConfiguration getCurrentScreen(String flow, String screen) {
    FlowConfiguration currentFlowConfiguration = flowConfigurations.stream().filter(
            flowConfiguration -> flowConfiguration.getName().equals(flow)
    ).toList().get(0);
    return currentFlowConfiguration.getScreenNavigation(screen);
  }
}
