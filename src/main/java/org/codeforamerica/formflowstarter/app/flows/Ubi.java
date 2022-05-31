package org.codeforamerica.formflowstarter.app.flows;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Ubi {

  @NotBlank(message = "{validation.make-sure-to-provide-a-first-name}")
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
