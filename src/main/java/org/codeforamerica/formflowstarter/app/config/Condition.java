package org.codeforamerica.formflowstarter.app.config;

import org.codeforamerica.formflowstarter.app.data.Submission;

public abstract class Condition {
  public abstract Boolean runCondition(Submission submission);
}
