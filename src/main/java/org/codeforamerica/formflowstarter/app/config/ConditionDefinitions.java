package org.codeforamerica.formflowstarter.app.config;

public class ConditionDefinitions {
  public static Boolean showThisPage() {
    return true;
  }

  public static Boolean dontShowThisPage() {
    return false;
  }

  public Boolean thisIsAnotherCondition() {
    return true;
  }

  public Boolean moreComplexCondition() {
    return showThisPage() && thisIsAnotherCondition();
  }
}
