package org.codeforamerica.formflowstarter.app.config;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLOutput;
import org.springframework.stereotype.Component;

@Component
public class ConditionHandler {
  Class<ConditionDefinitions> conditions = ConditionDefinitions.class;

  public Boolean handleCondition(String conditionName)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Boolean result = false;
    try {
      result = (Boolean) conditions.getMethod(conditionName).invoke(conditions);
    } catch (NoSuchMethodException e) {
      System.out.println("No such method could be found in the ConditionDefinitions class.");
    } catch (InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return result;
  }
}
