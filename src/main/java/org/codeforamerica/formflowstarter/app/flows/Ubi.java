package org.codeforamerica.formflowstarter.app.flows;

import java.util.ArrayList;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Ubi {

  // TODO: Delete when more "real" inputs are made
  // firstPage - test page for all inputs
//  @NotBlank(message = "{validations.make-sure-to-provide-a-first-name}")
//  @Size(min = 2, message = "{validations.test}")
  String firstName;
  String lastName;
  String emailAddress;
  String phoneNumber;
  String area;
  String dateDay;
  String dateMonth;
  String dateYear;
  String age2;
  @NotEmpty(message = "Please select at least one")
  ArrayList<String> gender;
  String favoriteColor;
  String favoriteFruit;
  String money;
  // /firstPage

  String languageRead;
  String languageSpoken;
  String needInterpreter;

  String birthDay;
  String birthMonth;
  String birthYear;
  String genderIdentity;
  String movedToUSA;
  String movedToUSADay;
  String movedToUSAMonth;
  String movedFromCountry;

  String hasHousehold;
  String householdMemberFirstName;
  String householdMemberLastName;
  String householdMemberRelationship;
  String householdMemberRecentlyMovedToUS;

  String householdMember;

  ArrayList<String> incomeTypes;
}
