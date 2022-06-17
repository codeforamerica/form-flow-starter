package org.codeforamerica.formflowstarter.testutilities;

import org.codeforamerica.formflowstarter.app.data.Submission;

public class TestConditionDefinitions {

  public boolean nameIsTestyMcTesterson(Submission submission) {
    if (submission.getInputData().containsKey("firstName")) {
      return submission.getInputData().get("firstName").equals("Testy McTesterson");
    }
    return false;
  }
}
