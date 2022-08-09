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
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codeforamerica.formflowstarter.app.config.ConditionHandler;
import org.codeforamerica.formflowstarter.app.config.FlowConfiguration;
import org.codeforamerica.formflowstarter.app.config.InputsConfiguration;
import org.codeforamerica.formflowstarter.app.config.NextScreen;
import org.codeforamerica.formflowstarter.app.config.ScreenNavigationConfiguration;
import org.codeforamerica.formflowstarter.app.config.SubflowConfiguration;
import org.codeforamerica.formflowstarter.app.data.Submission;
import org.codeforamerica.formflowstarter.app.data.SubmissionRepositoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
		if (currentScreen.getSubflow() != null) {
			String subflow = currentScreen.getSubflow();
			// Is subworkflow currently null? []
			if (!submission.getInputData().containsKey(subflow)) {
				return new ModelAndView("redirect:/%s/%s/new".formatted(flow, screen));
			}
		}
		Map<String, Object> model = createModel(flow, screen, httpSession, submission);
		return new ModelAndView("/%s/%s".formatted(flow, screen), model);
	}

	private Map<String, Object> createModel(String flow, String screen, HttpSession httpSession, Submission submission) {
		Map<String, Object> model = new HashMap<>();
		model.put("flow", flow);
		model.put("screen", screen);

		// Put subflow on model if on subflow delete confirmation screen
		HashMap<String, SubflowConfiguration> subflows = getFlowConfigurationByName(flow).getSubflows();
		if (subflows != null) {
			List<String> subflowFromDeleteConfirmationConfig = subflows
					.entrySet().stream().filter(entry ->
							entry.getValue().getDeleteConfirmationScreen().equals(screen)).map(Entry::getKey).toList();

			// Add the iteration start page to the model if we are on the review page for a subflow so we have it for the edit button
			subflows.forEach((key, value) -> {
				if (value.getReviewScreen().equals(screen)) {
					model.put("iterationStartScreen", value.getIterationStartScreen());
				}
			});


			if (!subflowFromDeleteConfirmationConfig.isEmpty()) {
				model.put("subflow", subflowFromDeleteConfirmationConfig.get(0));
			}
		}

		// if there's formDataSubmission
		if (httpSession.getAttribute("formDataSubmission") != null) {
			model.put("submission", submission);
			model.put("inputData", httpSession.getAttribute("formDataSubmission"));
		} else {
			model.put("submission", submission);
			model.put("inputData", submission.getInputData());
		}
		model.put("errorMessages", httpSession.getAttribute("errorMessages"));
		return model;
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
		var currentScreen = getCurrentScreen(flow, screen);
		var errorMessages = validationService.validate(flow, formDataSubmission);
		handleErrors(httpSession, errorMessages, formDataSubmission);

		if (errorMessages.size() > 0) {
			return new ModelAndView(String.format("redirect:/%s/%s", flow, screen));
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
			submission.setInputData(formDataSubmission);
			submissionRepositoryService.save(submission);
			httpSession.setAttribute("id", submission.getId());
		}

		return new ModelAndView(String.format("redirect:/%s/%s/navigation", flow, screen));
	}

	private void handleErrors(HttpSession httpSession, HashMap<String, ArrayList<String>> errorMessages, Map<String, Object> formDataSubmission) {
		if (errorMessages.size() > 0) {
			httpSession.setAttribute("errorMessages", errorMessages);
			httpSession.setAttribute("formDataSubmission", formDataSubmission);
		} else {
			httpSession.removeAttribute("errorMessages");
			httpSession.removeAttribute("formDataSubmission");
		}
	}

	// TODO: do we actually need this endpoint? How is is different from GET flow/screen?
	@GetMapping("{flow}/{screen}/new")
	ModelAndView getNewSubflow(
			@PathVariable String flow,
			@PathVariable String screen,
			HttpSession httpSession
	) {
		Submission submission = getSubmission(httpSession);
		Map<String, Object> model = createModel(flow, screen, httpSession, submission);

		return new ModelAndView(String.format("/%s/%s", flow, screen), model);
	}

	@PostMapping("{flow}/{screen}/new")
	ModelAndView postNewSubflow(
			@RequestParam(required = false) MultiValueMap<String, String> formData,
			@PathVariable String flow,
			@PathVariable String screen,
			HttpSession httpSession
	) {
//    Copy from OG /post request
		var formDataSubmission = removeEmptyValuesAndFlatten(formData);
		var submission = getSubmission(httpSession);
		var currentScreen = getCurrentScreen(flow, screen);
		var subflowName = getCurrentScreen(flow, screen).getSubflow();
		var errorMessages = validationService.validate(flow, formDataSubmission);
		handleErrors(httpSession, errorMessages, formDataSubmission);
		if (errorMessages.size() > 0) {
			return new ModelAndView(String.format("redirect:/%s/%s", flow, screen));
		}

		UUID uuid = UUID.randomUUID();
		formDataSubmission.put("uuid", uuid);

		// if there's already a session
		if (submission.getId() != null) {
			Map<String, Object> inputData = submission.getInputData();
			if (!submission.getInputData().containsKey(subflowName)) {
				submission.getInputData().put(subflowName, new ArrayList<Map<String, Object>>());
			}
			ArrayList<Map<String, Object>> subflow = (ArrayList<Map<String, Object>>) submission.getInputData().get(subflowName);
			subflow.add(formDataSubmission);

			submissionRepositoryService.save(submission);
		} else {
			submission.setFlow(flow);
			submission.setInputData(formDataSubmission);
			submissionRepositoryService.save(submission);
			httpSession.setAttribute("id", submission.getId());
		}

		return new ModelAndView(String.format("redirect:/%s/%s/navigation", flow, screen));
	}

	@GetMapping("{flow}/{subflow}/{uuid}/deleteConfirmation")
	RedirectView deleteConfirmation(
			@PathVariable String flow,
			@PathVariable String subflow,
			@PathVariable String uuid,
      HttpSession httpSession
  ) {
		String deleteConfirmationScreen = getFlowConfigurationByName(flow)
				.getSubflows().get(subflow).getDeleteConfirmationScreen();
		Long id = (Long) httpSession.getAttribute("id");
		if (id == null) {
			// we should throw an error here?
		}
		Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);

		if (submissionOptional.isPresent()) {
			Submission submission = submissionOptional.get();
			var existingInputData = submission.getInputData();
			var subflowArr = (ArrayList<Map<String, Object>>) existingInputData.get(subflow);
			var entryToDelete = subflowArr.stream().filter(entry -> entry.get("uuid").equals(uuid)).findFirst();
			entryToDelete.ifPresent(entry -> httpSession.setAttribute("entryToDelete", entry));
		}

		return new RedirectView(String.format("/%s/" + deleteConfirmationScreen + "?uuid=" + uuid, flow));
	}

	@PostMapping("{flow}/{subflow}/{uuid}/delete")
	ModelAndView deleteSubflowIteration(
			@RequestHeader("Referer") String referer,
			@PathVariable String flow,
			@PathVariable String subflow,
			@PathVariable String uuid,
			HttpSession httpSession
	) {
		Long id = (Long) httpSession.getAttribute("id");
		Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);
		String subflowEntryScreen = getFlowConfigurationByName(flow).getSubflows().get(subflow)
				.getEntryScreen();
		if (submissionOptional.isPresent()) {
			Submission submission = submissionOptional.get();
			var existingInputData = submission.getInputData();
			if (existingInputData.containsKey(subflow)) {
				var subflowArr = (ArrayList<Map<String, Object>>) existingInputData.get(subflow);
				subflowArr.remove(httpSession.getAttribute("entryToDelete"));
				httpSession.removeAttribute("entryToDelete");
				if (!subflowArr.isEmpty()) {
					existingInputData.put(subflow, subflowArr);
					submission.setInputData(existingInputData);
					submissionRepositoryService.save(submission);
				} else {
					existingInputData.remove(subflow);
					submission.setInputData(existingInputData);
					submissionRepositoryService.save(submission);
					return new ModelAndView("redirect:/%s/%s".formatted(flow, subflowEntryScreen));
				}
			} else {
				return new ModelAndView("redirect:/%s/%s".formatted(flow, subflowEntryScreen));
			}
		}
		String reviewScreen = getFlowConfigurationByName(flow).getSubflows().get(subflow)
				.getReviewScreen();
		return new ModelAndView(String.format("redirect:/%s/" + reviewScreen, flow));
	}

	// TODO: this only works for subflows with only one page to edit
	// Could change to flow/subflow/screen/:uuid/edit to better handle advanced cases
	// Potentially having a more generic end path to handle nested flow/subflows?
	@GetMapping("{flow}/{subflow}/{screen}/{uuid}/edit")
	ModelAndView getEditScreen(
			@PathVariable String flow,
			@PathVariable String subflow,
			@PathVariable String screen,
			@PathVariable String uuid,
			HttpSession httpSession
	) {
		String iterationStartScreen = getFlowConfigurationByName(flow)
				.getSubflows().get(subflow).getIterationStartScreen();
		Long id = (Long) httpSession.getAttribute("id");
		Map<String, Object> model;

		Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);
		if (submissionOptional.isPresent()) {
			Submission submission = submissionOptional.get();
			model = createModel(flow, iterationStartScreen, httpSession, submission);

			var existingInputData = submission.getInputData();
			var subflowArr = (ArrayList<Map<String, Object>>) existingInputData.get(subflow);
			var entryToEdit = subflowArr.stream()
					.filter(entry -> entry.get("uuid").equals(uuid)).findFirst();
			entryToEdit.ifPresent(stringObjectMap -> model.put("inputData", stringObjectMap));
			model.put("isEditScreen", true);
		} else {
			return new ModelAndView("/error", HttpStatus.BAD_REQUEST);
		}

		return new ModelAndView(String.format("%s/" + iterationStartScreen, flow), model);
	}

	@PostMapping("{flow}/{subflow}/{uuid}/edit")
	ModelAndView edit(
			@RequestParam(required = false) MultiValueMap<String, String> formData,
			@PathVariable String flow,
			@PathVariable String subflow,
			@PathVariable String uuid,
			HttpSession httpSession
	) {
		String iterationStartScreen = getFlowConfigurationByName(flow)
				.getSubflows().get(subflow).getIterationStartScreen();
		Long id = (Long) httpSession.getAttribute("id");
		Optional<Submission> submissionOptional = submissionRepositoryService.findById(id);
		Map<String, Object> formDataSubmission = removeEmptyValuesAndFlatten(formData);

		if (submissionOptional.isPresent()) {
			Submission submission = submissionOptional.get();
			var existingInputData = submission.getInputData();
			var subflowArr = (ArrayList<Map<String, Object>>) existingInputData.get(subflow);
			var iterationToEdit = subflowArr.stream()
					.filter(entry -> entry.get("uuid").equals(uuid)).findFirst();
			if (iterationToEdit.isPresent()) {
				Map<String, Object> iteration = iterationToEdit.get();
				iteration.forEach((key, value) -> {
					formDataSubmission.merge(key, value, (newValue, OldValue) -> newValue);
				});
				int indexToUpdate = subflowArr.indexOf(iteration);
				subflowArr.set(indexToUpdate, formDataSubmission);
				existingInputData.replace(subflow, subflowArr);
				submission.setInputData(existingInputData);
				submissionRepositoryService.save(submission);
			}
		} else {
			return new ModelAndView("/error", HttpStatus.BAD_REQUEST);
		}

		return new ModelAndView(String.format("redirect:/%s/%s/navigation", flow, iterationStartScreen));
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
						entry -> entry.getValue().size() == 1 && !entry.getKey().contains("[]")
								? entry.getValue().get(0) : entry.getValue()
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
		FlowConfiguration currentFlowConfiguration = getFlowConfigurationByName(flow);
		return currentFlowConfiguration.getScreenNavigation(screen);
	}

	private FlowConfiguration getFlowConfigurationByName(String flow) {
		return flowConfigurations.stream().filter(
				flowConfiguration -> flowConfiguration.getName().equals(flow)
		).toList().get(0);
	}

	private Boolean isConditionalNavigation(ScreenNavigationConfiguration currentScreen) {
		return currentScreen.getNextScreens().stream()
				.anyMatch(nextScreen -> nextScreen.getCondition() != null);
	}

	private List<NextScreen> getConditionalNextScreen(ScreenNavigationConfiguration currentScreen,
			HttpSession httpSession) {
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
