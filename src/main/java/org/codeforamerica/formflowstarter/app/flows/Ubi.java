package org.codeforamerica.formflowstarter.app.flows;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class Ubi {

  @NotBlank(message = "{validations.make-sure-to-provide-a-first-name}")
  @Size(min= 1, message = "{validations.test}")
  String firstName;

  @NotBlank
  String lastName;

  @Email
  String emailAddress;

  String phoneNumber;

  @NotBlank(message = "Fill out area!")
  String area;
  String dateDay;
  String dateMonth;
  String dateYear;
  String age2;
  String gender;
  String favoriteColor;
  String favoriteFruit;
  String money;
  String ssn;
}
