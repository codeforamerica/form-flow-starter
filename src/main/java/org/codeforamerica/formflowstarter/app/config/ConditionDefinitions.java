package org.codeforamerica.formflowstarter.app.config;

import java.util.ArrayList;
import java.util.Map;
import org.codeforamerica.formflowstarter.app.data.Submission;

public class ConditionDefinitions {

	public static Boolean applicantIsFemale(Submission submission) {
		var inputData = submission.getInputData();
		if (inputData.containsKey("gender[]")) {
			return ((ArrayList<String>) inputData.get("gender[]")).contains("FEMALE");
		}
		return false;
	}

	public static Boolean hasHousehold(Submission submission) {
		var inputData = submission.getInputData();
		if (inputData.containsKey("hasHousehold")) {
			return inputData.get("hasHousehold").equals("true");
		}
		return false;
	}

	public static Boolean incomeSelectedSelf(Submission submission, String uuid) {
		Map<String, Object> inputData = submission.getInputData();
		if (submission.getInputData().containsKey("income")) {
			// Change logic to suit your needs
			var incomeArr = (ArrayList<Map<String, Object>>) submission.getInputData().get("income");
			Map<String, Object> personsIncome = incomeArr.stream()
					.filter(entry -> entry.get("uuid").equals(uuid)).toList().get(0);
			return personsIncome.get("householdMember")
					.equals("applicant");
		}
		return false;
	}
}
