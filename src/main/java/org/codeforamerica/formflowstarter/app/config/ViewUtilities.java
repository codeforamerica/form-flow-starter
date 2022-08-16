package org.codeforamerica.formflowstarter.app.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.codeforamerica.formflowstarter.app.data.Submission;

public class ViewUtilities {

	public static List<Map<String, Object>> sortIncomeNamesWithApplicantFirst(Submission submission) {
		Map<String, Object> inputData = submission.getInputData();
		if (inputData.containsKey("income")) {
			ArrayList<Map<String, Object>> subflow = (ArrayList<Map<String, Object>>) submission.getInputData()
					.get("income");
			Stream<Map<String, Object>> applicantEntry = subflow.stream()
					.filter(entry -> entry.get("householdMember").equals("applicant"));
			Stream<Map<String, Object>> nonApplicantEntries = subflow.stream()
					.filter(entry -> !entry.get("householdMember").equals("applicant"));
			return Stream.concat(applicantEntry, nonApplicantEntries).toList();
		}

		return null;
	}

	public static String getIndividualsTotalIncome(Submission submission, String uuid) {
		if (submission.getInputData().containsKey("income")) {
			ArrayList<Map<String, Object>> incomeSubflow = (ArrayList<Map<String, Object>>) submission.getInputData()
					.get("income");
			Map<String, Object> individualsIncomeEntry = incomeSubflow.stream()
					.filter(entry -> entry.get("uuid").equals(uuid)).toList().get(0);
			ArrayList<String> incomeTypes = (ArrayList<String>) individualsIncomeEntry.get("incomeTypes[]");
			List<Integer> incomeTypeAmounts = incomeTypes.stream()
					.map(type -> Integer.parseInt((String) individualsIncomeEntry.get(type + "Amount")))
					.toList();
			if (incomeTypeAmounts.stream().reduce(Integer::sum).isPresent()) {
				return String.valueOf(incomeTypeAmounts.stream().reduce(Integer::sum).get());
			}
		}

		return null;
	}
}