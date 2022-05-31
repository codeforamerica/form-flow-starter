package org.codeforamerica.formflowstarter;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ValidationService {

  private final Validator validator;

  public ValidationService(Validator validator) {
    this.validator = validator;
  }

  public HashMap<String, String> validate(String flowName, Map<String, Object> formDataSubmission) {
    Class<?> clazz = null;
    try {
      clazz = Class.forName(
          "org.codeforamerica.formflowstarter.app.flows." + StringUtils.capitalize(flowName));
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }

    Class<?> flowClass = clazz;
    HashMap<String, String> validationMessages = new HashMap<>();
    formDataSubmission.forEach((key, value) -> {
      validator.validateValue(flowClass, key, value)
          .forEach(violation -> validationMessages.put(key, violation.getMessage()));
    });

    return validationMessages;
  }
}
