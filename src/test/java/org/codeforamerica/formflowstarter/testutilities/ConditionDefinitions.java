package org.codeforamerica.formflowstarter.testutilities;

import org.codeforamerica.formflowstarter.app.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class ConditionDefinitions {
  public static boolean nameIsTestyMcTesterson(Submission submission) {
    if (submission.getInputData().containsKey("firstName")) {
      return submission.getInputData().get("firstName").equals("Testy McTesterson");
    }
    return false;
  }
}
