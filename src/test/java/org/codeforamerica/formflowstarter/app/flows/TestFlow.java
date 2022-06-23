package org.codeforamerica.formflowstarter.app.flows;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestFlow {

  @NotBlank(message = "{validations.make-sure-to-provide-a-first-name}")
  String firstName;

  String textInput;
  String areaInput;
  String dateDay;
  String dateMonth;
  String dateYear;

  @NotBlank(message = "Don't leave this blank")
  @Size(min = 2, message = "You must enter a value 2 characters or longer")
  String inputWithMultipleValidations;

  @Size(min = 1, message = "You must select at least one fruit")
  List<String> favoriteFruitCheckbox;
}
