package org.codeforamerica.formflowstarter.app.flows;

import java.util.ArrayList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import lombok.Data;

@Data
public class Ubi {

  // TODO: Delete when more "real" inputs are made
  // firstPage - test page for all inputs
//  @NotBlank(message = "{validations.make-sure-to-provide-a-first-name}")
//  @Size(min = 2, message = "{validations.test}")\
  @NotBlank(message = "{personal-info.provide-first-name}")
  String firstName;
  @NotBlank(message = "{personal-info.provide-last-name}")
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
  @NotBlank
  String householdMemberFirstName;
  @NotBlank
  String householdMemberLastName;
  String householdMemberRelationship;
  String householdMemberRecentlyMovedToUS;

  String householdMember;

  @NotEmpty(message = "{income-types.error}")
  ArrayList<String> incomeTypes;
  @NotBlank(message = "{income-amounts.error}")
  @Pattern(regexp = "[0-9,\\.]", flags = {Flag.DOTALL})
  String incomeJobAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeSelfAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeUnemploymentAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeSocialSecurityAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeRetirementAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeChildOrSpousalSupportAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomePensionAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeInvestmentAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeCapitalGainsAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeRentalOrRoyaltyAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeFarmOrFishAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeAlimonyAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeTaxableScholarshipAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeCancelledDebtAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeCourtAwardsAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeGamblingAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeJuryDutyPayAmount;
  @NotBlank(message = "{income-amounts.error}")
  String incomeOtherAmount;
}
