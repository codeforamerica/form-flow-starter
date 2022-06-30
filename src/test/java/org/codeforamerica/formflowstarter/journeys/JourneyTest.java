package org.codeforamerica.formflowstarter.journeys;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.codeforamerica.formflowstarter.testutilities.AbstractBasePageTest;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.mock.mockito.MockBean;

public abstract class JourneyTest extends AbstractBasePageTest {

  protected String submissionId;

  @MockBean
  protected Clock clock;

  @Override
  @BeforeEach
  protected void setUp() throws IOException {
    super.setUp();
    driver.navigate().to(baseUrl);
    when(clock.instant()).thenReturn(Instant.now());
    when(clock.getZone()).thenReturn(ZoneOffset.UTC);
  }

//  @AfterEach
//  void tearDown() {
//    if (submissionId != null) {
//      Arrays.stream(Objects.requireNonNull(path.toFile().listFiles()))
//          .filter(file -> file.getName().contains(submissionId))
//          .forEach(File::delete);
//    }
//  }

//  protected String downloadPdfs() {
//    // Download CAF
//    SuccessPage successPage = new SuccessPage(driver);
//    successPage.downloadPdfZipFile();
//    await().until(zipDownloadCompletes(successPage));
//    unzipFiles();
//     var pdfs = getAllFiles();
//     caf = pdfs.getOrDefault(CAF, null);
//     ccap = pdfs.getOrDefault(CCAP,null);
//
//    return getApplicationId();
//  }

//  private String getApplicationId() {
//    // Retrieves the application id from the filename of a downloaded PDF
//    return Arrays.stream(Objects.requireNonNull(path.toFile().listFiles()))
//        .filter(file -> file.getName().endsWith(".pdf"))
//        .sorted((f1,f2)-> Long.compare(f2.lastModified(), f1.lastModified()))
//        .map(File::getName)
//        .findFirst()
//        .orElseThrow()
//        .split("_")[4];
//  }

//  @NotNull
//  protected Callable<Boolean> zipDownloadCompletes(SuccessPage successPage) {
//       return () -> getZipFile().size() == successPage.countDownloadLinks();
//  }

//  protected void fillOutHomeAndMailingAddress(String homeZip, String homeCity,
//      String homeStreetAddress, String homeApartmentNumber) {
//    testPage.enter("zipCode", homeZip);
//    testPage.enter("city", homeCity);
//    testPage.enter("streetAddress", homeStreetAddress);
//    testPage.enter("apartmentNumber", homeApartmentNumber);
//    testPage.clickContinue();
//
//    // Where can the county send your mail? (accept the smarty streets enriched address)
//    testPage.enter("zipCode", "23456");
//    testPage.enter("city", "someCity");
//    testPage.enter("streetAddress", "someStreetAddress");
//    testPage.enter("state", "IL");
//    testPage.enter("apartmentNumber", "someApartmentNumber");
//    when(smartyStreetClient.validateAddress(any())).thenReturn(
//        Optional.of(new Address("smarty street", "Cooltown", "CA", "03104", "1b", "someCounty"))
//    );
//    testPage.clickContinue();
//    testPage.clickElementById("enriched-address");
//    testPage.clickContinue();
//  }
  
  
//  protected void fillOutContactAndReview(boolean isReview) {
//    // Check that we get the no phone number confirmation screen if no phone number is entered
//    testPage.enter("email", "some@example.com");
//    testPage.clickContinue();
//    assertThat(testPage.getTitle()).contains("No phone number confirmation");
//    testPage.goBack();
//
//    // How can we get in touch with you?
//    testPage.enter("phoneNumber", "7234567890");
//    testPage.enter("email", "some@example.com");
//    assertThat(testPage.getCheckboxValues("phoneOrEmail")).contains("It's okay to text me",
//        "It's okay to email me");
//    testPage.clickContinue();
//
//    if (isReview)
//    {
//      // Let's review your info
//      assertThat(driver.findElement(By.id("mailingAddress-address_street")).getText())
//          .isEqualTo("smarty street");
//    }
//
//  }

//  protected void deleteAFile() {
//    await().until(
//		  () -> driver.findElements(By.className("dz-remove")).get(0).getAttribute("innerHTML")
//		  .contains("delete"));
//    testPage.clickLink("delete");
//
//    assertThat(testPage.getTitle()).isEqualTo("Delete a file");
//    testPage.clickButton("Yes, delete the file");
//  }

  protected void waitForErrorMessage() {
    WebElement errorMessage = driver.findElement(By.className("text--error"));
    await().until(() -> !errorMessage.getText().isEmpty());
  }
}
