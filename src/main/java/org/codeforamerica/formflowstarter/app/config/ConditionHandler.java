package org.codeforamerica.formflowstarter.app.config;

import java.lang.reflect.InvocationTargetException;
import lombok.Data;
import org.codeforamerica.formflowstarter.app.data.Submission;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConditionHandler {
  Class<ConditionDefinitions> conditions = ConditionDefinitions.class;
  Submission submission;

  public Boolean handleCondition(String conditionName)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Boolean result = false;
    try {
      result = (Boolean) conditions.getMethod(conditionName, Submission.class).invoke(conditions, submission);
    } catch (NoSuchMethodException e) {
      System.out.println("No such method could be found in the ConditionDefinitions class.");
    } catch (InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return result;
  }
}
