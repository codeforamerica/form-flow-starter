package org.codeforamerica.formflowstarter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
  private final SubmissionService service;


  public ScreenController(
      List<FlowConfiguration> flowConfigurations,
      ApplicationData applicationData,
      InputsConfiguration inputsConfiguration,
      SubmissionService service) {
    this.flowConfigurations = flowConfigurations;
    this.applicationData = applicationData;
    this.inputsConfiguration = inputsConfiguration;
    this.service = service;
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
      @RequestParam(required = false) MultiValueMap<String, String> model,
      @PathVariable String flow,
      @PathVariable String screen,
      HttpSession httpSession
  ) {
    // get screen (configuration)
    var currentScreen = getCurrentScreen(flow, screen);

    // if there's already a session
    Long id = (Long) httpSession.getAttribute("id");
    if (id != null) {
      Optional<Submission> submission = service.findById(id);
      if (submission.isPresent()) {
        Submission s = submission.get();

        model.entrySet().stream().flatMap(entry -> {
          entry.
          return entry.getValue().size() == 1 ? entry.getValue().get(0) : entry.getValue();
        });

        s.setInputData(model);
        service.save(s);
      }

    }
        // update it
    // else, create a new submission record
    var submission = new Submission();
    submission.setFlow(flow);
    service.save(submission);
    httpSession.setAttribute("id", submission.getId());

    // set the session's "id" to submission's ID.



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
