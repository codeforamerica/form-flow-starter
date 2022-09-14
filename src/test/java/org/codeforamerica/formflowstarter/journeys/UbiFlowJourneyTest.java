package org.codeforamerica.formflowstarter.journeys;

import static org.assertj.core.api.Assertions.assertThat;
import static org.codeforamerica.formflowstarter.testutilities.YesNoAnswer.NO;
import static org.codeforamerica.formflowstarter.testutilities.YesNoAnswer.YES;

import java.util.List;
import org.codeforamerica.formflowstarter.testutilities.PercyTestPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("fullFlowJourney")
public class UbiFlowJourneyTest extends JourneyTest {

  protected void initTestPage() {
    testPage = new PercyTestPage(driver);
  }

  @Test
  void fullUbiFlow() {
    // Landing screen
    assertThat(testPage.getTitle()).isEqualTo("Apply for UBI payments easily online.");
    testPage.clickButton("Apply now");
    // How this works screen
    testPage.clickContinue();
    // Language preference
    testPage.clickContinue();
    // Getting to know you
    testPage.clickContinue();
    // Personal info
    testPage.enter("firstName", "Testy");
    testPage.enter("lastName", "McTesterson");
    testPage.clickContinue();
    // Eligibility screen
    testPage.clickContinue();
    // Housemates screen
    testPage.enter("hasHousehold", NO.getDisplayValue());
    // Income screen
    assertThat(testPage.getTitle()).isEqualTo("Income");
    // Go back to household page and select yes instead
    testPage.goBack();
    testPage.enter("hasHousehold", YES.getDisplayValue());
    // Housemate Info screen
    testPage.enter("householdMemberFirstName", "John");
    testPage.enter("householdMemberLastName", "Doe");
    testPage.clickContinue();
    // Household List screen
    testPage.clickButton("+ Add a person");
    // Housemate Info screen
    testPage.enter("householdMemberFirstName", "Jane");
    testPage.enter("householdMemberLastName", "Doe");
    testPage.clickContinue();
    // Two household members are present
    assertThat(testPage.getCssSelectorText(".form-card__content")).contains("John Doe");
    assertThat(testPage.getCssSelectorText(".form-card__content")).contains("Jane Doe");
    // Delete Jane Doe
    testPage.findElementsByClass("subflow-delete").get(1).click();
    testPage.clickButton("Yes, remove them");
    assertThat(testPage.getCssSelectorText(".form-card__content")).doesNotContain("Jane Doe");
    assertThat(testPage.findElementsByClass("subflow-delete")).hasSize(1);
    // Go back to delete confirmation and make sure someone else isn't deleted
    testPage.goBack();
    assertThat(testPage.getHeader()).contains("This entry has already been deleted");
    testPage.clickButton("Return to the screen I was on before");
    assertThat(testPage.getHeader()).contains("Is this everyone that lives with you?");
    assertThat(testPage.findElementsByClass("subflow-delete")).hasSize(1);
    // Delete final household member to go back to householdList
    testPage.findElementsByClass("subflow-delete").get(0).click();
    testPage.clickButton("Yes, remove them");
    assertThat(testPage.getTitle()).isEqualTo("Housemates");
    // Add back household members
    testPage.enter("hasHousehold", YES.getDisplayValue());
    // Housemate Info screen
    testPage.enter("householdMemberFirstName", "John");
    testPage.enter("householdMemberLastName", "Doe");
    testPage.clickContinue();
    // Household List screen
    testPage.clickButton("+ Add a person");
    // Housemate Info screen
    testPage.enter("householdMemberFirstName", "Jane");
    testPage.enter("householdMemberLastName", "Doe");
    testPage.clickContinue();
    // Edit a person
    testPage.findElementsByClass("subflow-edit").get(0).click();
    testPage.enter("householdMemberFirstName", "Anthony");
    testPage.enter("householdMemberLastName", "Dee");
    testPage.clickContinue();
    assertThat(testPage.getCssSelectorText(".form-card__content")).contains("Anthony Dee");
    assertThat(testPage.getCssSelectorText(".form-card__content")).doesNotContain("John Doe");
    testPage.clickButton("Yes, this is everyone");

    //click on No I already know....
    assertThat(testPage.getTitle()).isEqualTo("Income");
    testPage.clickLink("No, I already know my annual household pre-tax income - I prefer to enter it directly.");
    // Reported Annual Household Income screen
    assertThat(testPage.getTitle()).isEqualTo("Reported Annual Household Pre-Tax Income");
    testPage.enter("reportedTotalAnnualHouseholdIncome", "125");
    testPage.clickContinue();
    // Income Complete Screen
    assertThat(testPage.getTitle()).isEqualTo("Income Complete");
    // Go Back and Add Income for Household
    testPage.goBack();
    testPage.goBack();
    assertThat(testPage.getTitle()).isEqualTo("Income");
    // Add income screen
    testPage.clickButton("Add income");
    // Household Member Income screen
    testPage.clickElementById("householdMember-applicant");
    testPage.clickContinue();
    // Income Types screen
    testPage.enter("incomeTypes", List.of("incomeSelf", "incomeJob", "incomeInvestment"));
    testPage.clickContinue();
    // Income Amounts screen
    testPage.enter("incomeSelfAmount", "10");
    testPage.enter("incomeJobAmount", "50");
    testPage.enter("incomeInvestmentAmount", "20");
    testPage.clickContinue();
    // Annual Household Income screen
    assertThat(testPage.findElementById("applicant-amount").getText()).isEqualTo("$80.00");
    testPage.clickLink("+ Add income for another household member");
    testPage.clickElementById("householdMember-Anthony Dee");
    testPage.clickContinue();
    // Income Types screen
    testPage.enter("incomeTypes", List.of("incomeSelf", "incomeJob", "incomeInvestment"));
    testPage.clickContinue();
    // Income Amounts screen
    testPage.enter("incomeSelfAmount", "20");
    testPage.enter("incomeJobAmount", "30");
    testPage.enter("incomeInvestmentAmount", "10");
    testPage.clickContinue();
    // Annual Household Income screen
    assertThat(testPage.findElementById("anthony-dee-amount").getText()).isEqualTo("$60.00");
    testPage.clickLink("+ Add income for another household member");
    testPage.clickElementById("householdMember-Jane Doe");
    testPage.clickContinue();
    // Income Types screen
    testPage.enter("incomeTypes", List.of("incomeSelf", "incomeJob", "incomeInvestment"));
    testPage.clickContinue();
    // Income Amounts screen
    testPage.enter("incomeSelfAmount", "10");
    testPage.enter("incomeJobAmount", "20");
    testPage.enter("incomeInvestmentAmount", "30");
    testPage.clickContinue();
    // Annual Household Income screen
    assertThat(testPage.findElementById("anthony-dee-amount").getText()).isEqualTo("$60.00");
    assertThat(testPage.findElementById("household-total-income").getText()).isEqualTo("$200.00");
  }

// Assert intercom button is present on landing page
//  await().atMost(5, SECONDS).until(() -> !driver.findElements(By.id("intercom-frame")).isEmpty());
//  assertThat(driver.findElement(By.id("intercom-frame"))).isNotNull();

//  private void assertFileDetailsAreCorrect(List<WebElement> filenameTextElements,
//      List<WebElement> fileDetailsElements, int index,
//      String filenameWithoutExtension, String extension, String size,
//      String sizeUnit) {
//    // test-caf.pdf
//    var filename = getAttributeForElementAtIndex(filenameTextElements, index, "innerHTML");
//    var fileDetails = getAttributeForElementAtIndex(fileDetailsElements, index, "innerHTML");
//
//    assertThat(filename).contains(filenameWithoutExtension);
//    assertThat(filename).contains(extension);
//    assertThat(fileDetails).contains(size);
//    assertThat(fileDetails).contains(sizeUnit);
//  }

//  private void assertStylingOfNonEmptyDocumentUploadPage() {
//    assertThat(driver.findElement(By.id("drag-and-drop-box")).getAttribute("class")).contains(
//        "drag-and-drop-box-compact");
//    assertThat(driver.findElement(By.id("upload-button"))
//        .getAttribute("class")).contains("grid--item width-one-third");
//    assertThat(driver.findElement(By.id("vertical-header-desktop")).getAttribute("class"))
//        .contains("hidden");
//    assertThat(driver.findElement(By.id("vertical-header-mobile")).getAttribute("class"))
//        .contains("hidden");
//    assertThat(driver.findElement(By.id("horizontal-header-desktop")).getAttribute("class"))
//        .doesNotContain("hidden");
//    assertThat(driver.findElement(By.id("horizontal-header-mobile")).getAttribute("class"))
//        .doesNotContain("hidden");
//    assertThat(driver.findElement(By.id("upload-doc-div")).getAttribute("class"))
//        .doesNotContain("hidden");
//  }

//  private void assertStylingOfEmptyDocumentUploadPage() {
//    assertThat(driver.findElement(By.id("drag-and-drop-box")).getAttribute("class")).doesNotContain(
//        "drag-and-drop-box-compact");
//    assertThat(driver.findElement(By.id("upload-button")).getAttribute("class")).doesNotContain(
//        "grid--item width-one-third");
//    assertThat(driver.findElement(By.id("vertical-header-desktop")).getAttribute("class"))
//        .doesNotContain("hidden");
//    assertThat(driver.findElement(By.id("vertical-header-mobile")).getAttribute("class"))
//        .doesNotContain("hidden");
//    assertThat(driver.findElement(By.id("horizontal-header-desktop")).getAttribute("class"))
//        .contains("hidden");
//    assertThat(driver.findElement(By.id("horizontal-header-mobile")).getAttribute("class"))
//        .contains("hidden");
//    assertThat(driver.findElement(By.id("upload-doc-div")).getAttribute("class")).contains(
//        "hidden");
//  }
}
