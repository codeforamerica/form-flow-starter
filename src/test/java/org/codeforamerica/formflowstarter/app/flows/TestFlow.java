package org.codeforamerica.formflowstarter.app.flows;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
  String numberInput;
  List<String> checkboxSet;
  String checkboxInput;
  String radioInput;
  String selectInput;
  String moneyInput;
  String phoneInput;
  String ssnInput;
  @NotEmpty(message = "Please select at least one")
  List<String> favoriteFruitCheckbox;

  @NotBlank(message = "Don't leave this blank")
  @Size(min = 2, message = "You must enter a value 2 characters or longer")
  String inputWithMultipleValidations;

  String householdMemberFirstName;
  String householdMemberLastName;
  String householdMemberRelationship;
  String householdMemberRecentlyMovedToUS;
}
