package org.codeforamerica.formflowstarter.app.config;

import lombok.Data;
import org.codeforamerica.formflowstarter.app.data.Submission;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Data
@Component
public class SubmissionHandler {
  Class<SubmissionActions> submissionActions = SubmissionActions.class;
  Submission submission;

  public void handleSubmission(String beforeSaveAction)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    try {
      submissionActions.getMethod(beforeSaveAction, Submission.class).invoke(submissionActions, submission);
    } catch (NoSuchMethodException e) {
      System.out.println("No such method could be found in the SubmissionActions class.");
    } catch (InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
