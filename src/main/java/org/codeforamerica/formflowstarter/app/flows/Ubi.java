package org.codeforamerica.formflowstarter.app.flows;

import java.util.ArrayList;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeJobAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeSelfAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeUnemploymentAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeSocialSecurityAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeRetirementAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeChildOrSpousalSupportAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomePensionAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeInvestmentAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeCapitalGainsAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeRentalOrRoyaltyAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeFarmOrFishAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeAlimonyAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeTaxableScholarshipAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeCancelledDebtAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeCourtAwardsAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeGamblingAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeJuryDutyPayAmount;
  @NotBlank(message = "{income-amounts.must-select-one}")
  @DecimalMin(value = "0.0", message = "{income-amounts.must-be-numbers}")
  String incomeOtherAmount;

  @NotBlank(message = "{household-reported-annual-pre-tax-income.please-enter-a-value}")
  @DecimalMin(value = "0.0", message = "{household-reported-annual-pre-tax-income.must-be-a-valid-amount}")
  String reportedTotalAnnualHouseholdIncome;
}
