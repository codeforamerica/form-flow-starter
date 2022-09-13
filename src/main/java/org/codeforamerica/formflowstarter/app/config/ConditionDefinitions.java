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

  public static Boolean householdMemberAlreadyHasIncome(Submission submission,
      String householdMemberName) {
    if (submission.getInputData().containsKey("income")) {
      var incomeArr = (ArrayList<Map<String, Object>>) submission.getInputData().get("income");
      var memberItationOptional = incomeArr.stream().filter(entry ->
              entry.get("householdMember").equals(householdMemberName)).findFirst();
      if (memberItationOptional.isPresent()) {
        return (Boolean) memberItationOptional.get().get("isSubflowComplete");
      }
    }
    return false;
  }

  public static Boolean allHouseholdMembersHaveIncome(Submission submission) {
    if (submission.getInputData().containsKey("household") && submission.getInputData()
        .containsKey("income")) {
      var householdArr = (ArrayList<Map<String, Object>>) submission.getInputData()
          .get("household");
      var incomeArr = (ArrayList<Map<String, Object>>) submission.getInputData().get("income");

      // household members + applicant
      return (householdArr.size() + 1) == incomeArr.size();
    } else if (!submission.getInputData().containsKey("household") && submission.getInputData().containsKey("income")){
      return true;
    }
    return false;
  }
}
