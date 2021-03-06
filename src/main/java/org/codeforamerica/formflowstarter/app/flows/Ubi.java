package org.codeforamerica.formflowstarter.app.flows;

import java.util.ArrayList;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Ubi {

//  @NotBlank(message = "{validations.make-sure-to-provide-a-first-name}")
//  @Size(min = 2, message = "{validations.test}")
  String firstName;

//  @NotBlank
  String lastName;

//  @Email
  String emailAddress;

//  @NotBlank(message = "Fill out phone")
  String phoneNumber;

//  @NotBlank(message = "Fill out area!")
  String area;

//  @NotBlank(message = "Fill out a day")
//  @Size(min = 1, max = 2, message = "Day must not be more than two characters")
  String dateDay;

//  @NotBlank(message = "Fill out a month")
//  @Size(min = 1, max = 2, message = "Month must not be more than two characters")
  String dateMonth;

//  @NotBlank(message = "Fill out a year")
//  @Size(min = 4, max = 4, message = "Year must be exactly four characters")
  String dateYear;

//  @NotBlank(message = "Fill out age2")
  String age2;

  @NotEmpty(message = "Please select at least one")
  ArrayList<String> gender;

//  @NotEmpty(message = "Fill out favorite color")
  String favoriteColor;

//  @NotEmpty(message = "Fill out favorite fruit")
  String favoriteFruit;

//  @NotBlank(message = "Fill out money")
  String money;

  String ssn;
  String applicantSsn;
  String householdMemberSsn;
  String householdMemberSsn2;

  String hasHousehold;
  String householdMemberFirstName;
  String householdMemberLastName;
  String householdMemberRelationship;
  String householdMemberRecentlyMovedToUS;
}
