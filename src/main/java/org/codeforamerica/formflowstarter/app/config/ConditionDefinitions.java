package org.codeforamerica.formflowstarter.app.config;

import org.codeforamerica.formflowstarter.app.data.Submission;

public class ConditionDefinitions {

  // firstName, lastName, email, address
  public static boolean applicantIsFemale(Submission submission) {
    // TODO: Tried creating helper methods, but became too complex, waiting to see if a better pattern emerges
    // Null check on fieldName
    if (submission.getInputData().containsKey("gender")) {
      // Condition logic
      return submission.getInputData().get("gender").equals("FEMALE");
    }
    return false;
  }

  public static boolean dontShowThisPage() {
    return false;
  }

  public boolean thisIsAnotherCondition() {
    return true;
  }
}
