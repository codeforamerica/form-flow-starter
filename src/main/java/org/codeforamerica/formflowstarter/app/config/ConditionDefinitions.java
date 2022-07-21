package org.codeforamerica.formflowstarter.app.config;

import java.util.ArrayList;
import org.codeforamerica.formflowstarter.app.data.Submission;

public class ConditionDefinitions {

  public static boolean applicantIsFemale(Submission submission) {
    var inputData = submission.getInputData();
    if (inputData.containsKey("gender[]")) {
      return ((ArrayList<String>) inputData.get("gender[]")).contains("FEMALE");
    }
    return false;
  }

  public static boolean hasHousehold(Submission submission) {
    var inputData = submission.getInputData();
    if (inputData.containsKey("hasHousehold")) {
      return inputData.get("hasHousehold").equals("true");
    }
    return false;
  }
}
