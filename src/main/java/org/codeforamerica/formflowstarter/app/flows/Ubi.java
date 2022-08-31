package org.codeforamerica.formflowstarter.app.flows;

import java.util.ArrayList;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Ubi {

  // Needs to be declared since Spring Security inserts _csrf as a hidden field to all forms
  String _csrf;
  // TODO: Delete when more "real" inputs are made
  // firstPage - test page for all inputs
  @NotBlank(message = "{personal-info.provide-first-name}")
  String firstName;
  @NotBlank(message = "{personal-info.provide-last-name}")
  String lastName;

  // Language Preferences Screen
  String languageRead;
  String languageSpoken;
  String needInterpreter;

  // Personal Info Screen
  String birthDay;
  String birthMonth;
  String birthYear;
  String genderIdentity;
  String movedToUSA;
  String movedToUSADay;
  String movedToUSAMonth;
  // TODO: figure out how to only have day & month for a date fragment
  String movedToUSAYear;
  String movedFromCountry;

  // Housemates Screen
  String hasHousehold;

  // Housemate Info Screen
  @NotBlank
  String householdMemberFirstName;
  @NotBlank
  String householdMemberLastName;
  String householdMemberRelationship;
  String householdMemberRecentlyMovedToUS;

  // Household Member Income Screen
  String householdMember;

  // Income Types Screen
  @NotEmpty(message = "{income-types.error}")
  ArrayList<String> incomeTypes;

  // Income Amounts Screen
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

  // Reported Household Annual Income Screen
  @NotBlank(message = "{household-reported-annual-pre-tax-income.please-enter-a-value}")
  @DecimalMin(value = "0.0", message = "{household-reported-annual-pre-tax-income.must-be-a-valid-amount}")
  String reportedTotalAnnualHouseholdIncome;
}
