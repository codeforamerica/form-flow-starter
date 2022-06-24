package org.codeforamerica.formflowstarter.app.flows;

import com.github.microtweak.validator.conditional.core.constraint.NotEmptyWhen;
import java.util.ArrayList;
import com.github.microtweak.validator.conditional.core.ConditionalValidate;
import com.github.microtweak.validator.conditional.core.constraint.NotNullWhen;
import com.github.microtweak.validator.conditional.core.constraint.SizeWhen;
import java.util.ArrayList;
import java.util.Arrays;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@ConditionalValidate
//@NotNullIfAnotherFieldHasValue(
//    fieldName = "useEmail",
//    fieldValue = "true",
//    dependFieldName = "emailAddress")
//@NotNullIfAnotherFieldHasValue(
//    fieldName = "userSms",
//    fieldValue = "true",
//    dependFieldName = "phoneNumber")
public class Ubi {

  @NotBlank(message = "{validations.make-sure-to-provide-a-first-name}")
  @Size(min = 2, message = "{validations.test}")
  String firstName;

  Boolean testCondition;

//  @NotEmptyWhen(expression = "null")
//  @NotEmpty
  String lastName;

// No idea how this would get called
//  @AssertTrue(message = "Please fill out email")
//  public boolean isEmail() {
//    if (this.testCondition) {
//      return this.lastName != null;
//    }
//
//    return false;
//  }

  @Email
  String emailAddress;

  @NotBlank(message = "Fill out phone")
  String phoneNumber;

  @NotBlank(message = "Fill out area!")
  String area;

  @NotBlank(message = "Fill out a day")
  @Size(min = 1, max = 2, message = "Day must not be more than two characters")
  String dateDay;

  @NotBlank(message = "Fill out a month")
  @Size(min = 1, max = 2, message = "Month must not be more than two characters")
  String dateMonth;

  @NotBlank(message = "Fill out a year")
  @Size(min = 4, max = 4, message = "Year must be exactly four characters")
  String dateYear;

  @NotBlank(message = "Fill out age2")
  String age2;

  @Size(min = 2, max = 2)
  ArrayList<String> gender;

  @NotEmpty(message = "Fill out favorite color")
  String favoriteColor;

  @NotEmpty(message = "Fill out favorite fruit")
  String favoriteFruit;

  @NotBlank(message = "Fill out money")
  String money;

  String ssn;
}


//public class SampleClass {
//
//  private Boolean useEmail;
//  private String emailAddress;
//
//  @AssertTrue(message = "Please fill out email")
//  public boolean isEmail() {
//    if (this.useEmail) {
//      if (this.emailAddress != null)
//        return true;
//    }
//
//    return false;
//  }
//}
