package org.codeforamerica.formflowstarter.journeys;

import static org.assertj.core.api.Assertions.assertThat;

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
    assertThat(testPage.getTitle()).isEqualTo("Apply for UBI payments easily online.");
    testPage.clickButton("Apply now");
    // How this works page
    testPage.clickContinue();
    // Language Prefs page

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
