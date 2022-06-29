package org.codeforamerica.formflowstarter.app.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.codeforamerica.formflowstarter.app.data.Submission;
import org.codeforamerica.formflowstarter.app.data.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@Sql(statements = {"ALTER SEQUENCE submissions_id_seq RESTART WITH 12", "TRUNCATE TABLE submissions"})
@SpringBootTest
class SubmissionRepositoryTest {

  @Autowired
  private SubmissionRepository submissionRepository;

  @Test
  void shouldSaveSubmissionsWithSequentialIds() {
    Submission firstSubmission = new Submission();
    Submission secondSubmission = new Submission();
    firstSubmission.setFlow("testFlow");
    secondSubmission.setFlow("testFlow");
    submissionRepository.save(firstSubmission);
    submissionRepository.save(secondSubmission);
    assertThat(firstSubmission.getId()).isEqualTo(12);
    assertThat(secondSubmission.getId()).isEqualTo(13);
  }

  @Test
  void shouldSaveSubmission() {

    Submission submission = Submission.builder()
        .id(123L)
        .inputData(Map.of(
            "testKey", "this is a test value",
            "otherTestKey", List.of("A", "B", "C")))
        .createdAt(Date.from(Instant.now().minus(Duration.ofDays(10))))
        .flow("testFlow")
        .build();

    submissionRepository.save(submission);

    Optional<Submission> savedSubmissionOptional = submissionRepository.findById(123L);
    Submission savedSubmission = null;
    if (savedSubmissionOptional.isPresent()) {
      savedSubmission = savedSubmissionOptional.get();
    }
    assertThat(savedSubmission.getId()).isEqualTo(submission.getId());
    assertThat(savedSubmission.getFlow()).isEqualTo("foo");
    assertThat(savedSubmission.getCreatedAt()).isEqualTo(submission.getCreatedAt());
    assertThat(savedSubmission.getInputData()).isEqualTo(submission.getInputData());
    assertThat(submission.getSubmittedAt()).isNotNull();
    assertThat(savedSubmission.getSubmittedAt()).isNotNull();
  }

//  @Test
//  void shouldPrefixIdWithRandom3DigitSalt() {
//    String nextId = applicationRepository.getNextId();
//
//    assertThat(nextId).matches("^[1-9]\\d{2}.*");
//
//    String nextIdAgain = applicationRepository.getNextId();
//
//    assertThat(nextIdAgain.substring(0, 3)).isNotEqualTo(nextId.substring(0, 3));
//  }
//
//  @Test
//  void shouldPadTheIdWithZeroesUntilReach10Digits() {
//    String nextId = applicationRepository.getNextId();
//
//    assertThat(nextId).hasSize(10);
//    assertThat(nextId.substring(3, 8)).isEqualTo("00000");
//  }
//
//  @Test
//  void shouldSaveApplicationWithOptionalFieldsPopulated() {
//    ApplicationData applicationData = new TestApplicationDataBuilder()
//        .withPageData("somePage", "someInput", emptyList()).build();
//
//    Application application = Application.builder()
//        .id("someid")
//        .completedAt(ZonedDateTime.now(UTC))
//        .applicationData(applicationData)
//        .county(Olmsted)
//        .timeToComplete(Duration.ofSeconds(12415))
//        .sentiment(Sentiment.HAPPY)
//        .feedback("so so happy")
//        .flow(FlowType.FULL)
//        .build();
//
//    applicationRepository.save(application);
//
//    Application savedApplication = applicationRepository.find("someid");
//    assertThat(savedApplication.getSentiment()).isEqualTo(application.getSentiment());
//    assertThat(savedApplication.getFeedback()).isEqualTo(application.getFeedback());
//    assertThat(savedApplication.getFlow()).isEqualTo(FlowType.FULL);
//  }
//
//  @Test
//  void shouldUpdateExistingApplication() {
//    ApplicationData applicationData = new TestApplicationDataBuilder()
//        .withPageData("somePage", "someInput", emptyList()).build();
//
//    String applicationId = "someid";
//    Application application = Application.builder()
//        .id(applicationId)
//        .applicationData(applicationData)
//        .county(Olmsted)
//        .timeToComplete(Duration.ofSeconds(12415))
//        .sentiment(Sentiment.MEH)
//        .feedback("someFeedback")
//        .flow(FlowType.FULL)
//        .build();
//
//    applicationRepository.save(application);
//
//    ApplicationData updatedApplicationData = new TestApplicationDataBuilder()
//        .withPageData("someUpdatedPage", "someUpdatedInput", emptyList()).build();
//    ZonedDateTime completedAt = ZonedDateTime.now(UTC).truncatedTo(ChronoUnit.MILLIS);
//    Application updatedApplication = Application.builder()
//        .id(application.getId())
//        .completedAt(completedAt)
//        .applicationData(updatedApplicationData)
//        .county(Hennepin)
//        .timeToComplete(Duration.ofSeconds(421))
//        .sentiment(Sentiment.HAPPY)
//        .feedback("someUpdatedFeedback")
//        .flow(FlowType.EXPEDITED)
//        .applicationStatuses(emptyList())
//        .build();
//
//    applicationRepository.save(updatedApplication);
//
//    Application retrievedApplication = applicationRepository.find(applicationId);
//
//    assertThat(retrievedApplication).usingRecursiveComparison()
//        .ignoringFields("fileName", "updatedAt").isEqualTo(updatedApplication);
//  }
//
//  @Nested
//  class EncryptionAndDecryptionTest extends AbstractRepositoryTest {
//
//    ApplicationRepository applicationRepositoryWithMockEncryptor;
//    @SuppressWarnings("unchecked")
//    Encryptor<ApplicationData> mockEncryptor = mock(Encryptor.class);
//    String jsonData = "\"{here: 'is the encrypted data'}\"";
//
//    @BeforeEach
//    void setUp() {
//      applicationRepositoryWithMockEncryptor = new ApplicationRepository(jdbcTemplate,
//          mockEncryptor);
//      when(mockEncryptor.encrypt(any())).thenReturn(jsonData);
//    }
//
//    @Test
//    void shouldEncryptApplicationData() {
//      ApplicationData applicationData = new ApplicationData();
//      Application application = Application.builder()
//          .id("someid")
//          .completedAt(ZonedDateTime.now(UTC))
//          .applicationData(applicationData)
//          .county(Olmsted)
//          .timeToComplete(Duration.ofSeconds(1))
//          .build();
//
//      applicationRepositoryWithMockEncryptor.save(application);
//
//      verify(mockEncryptor).encrypt(applicationData);
//    }
//
//    @Test
//    void shouldStoreEncryptedApplicationData() {
//      ApplicationData applicationData = new ApplicationData();
//      Application application = Application.builder()
//          .id("someid")
//          .completedAt(ZonedDateTime.now(UTC))
//          .applicationData(applicationData)
//          .county(Olmsted)
//          .timeToComplete(Duration.ofSeconds(1))
//          .build();
//
//      applicationRepositoryWithMockEncryptor.save(application);
//
//      String actualEncryptedData = jdbcTemplate.queryForObject(
//          "SELECT application_data " +
//              "FROM applications " +
//              "WHERE id = 'someid'", String.class);
//      assertThat(actualEncryptedData).isEqualTo(jsonData);
//    }
//
//    @Test
//    void shouldDecryptApplicationData() {
//      ApplicationData applicationData = new ApplicationData();
//      String applicationId = "someid";
//      Application application = Application.builder()
//          .id(applicationId)
//          .completedAt(ZonedDateTime.now(UTC))
//          .applicationData(applicationData)
//          .county(Olmsted)
//          .timeToComplete(Duration.ofSeconds(1))
//          .build();
//      ApplicationData decryptedApplicationData = new TestApplicationDataBuilder()
//          .withPageData("somePage", "someInput", "CASH").build();
//      when(mockEncryptor.decrypt(any())).thenReturn(decryptedApplicationData);
//
//      applicationRepositoryWithMockEncryptor.save(application);
//
//      applicationRepositoryWithMockEncryptor.find(applicationId);
//
//      verify(mockEncryptor).decrypt(jsonData);
//    }
//
//    @Test
//    void shouldUseDecryptedApplicationDataForTheRetrievedApplication() {
//      ApplicationData applicationData = new ApplicationData();
//      applicationData.setPagesData(new PagesData(Map.of("somePage", new PageData())));
//      String applicationId = "someid";
//      Application application = Application.builder()
//          .id(applicationId)
//          .completedAt(ZonedDateTime.now(UTC))
//          .applicationData(applicationData)
//          .county(Olmsted)
//          .timeToComplete(Duration.ofSeconds(1))
//          .build();
//      ApplicationData decryptedApplicationData = new TestApplicationDataBuilder()
//          .withPageData("somePage", "someInput", "CASH").build();
//
//      when(mockEncryptor.decrypt(any())).thenReturn(decryptedApplicationData);
//
//      applicationRepositoryWithMockEncryptor.save(application);
//
//      Application retrievedApplication = applicationRepositoryWithMockEncryptor.find(applicationId);
//      assertThat(retrievedApplication.getApplicationData()).isEqualTo(decryptedApplicationData);
//    }
//  }
//
//  @Test
//  void shouldReturnAppsWithNoStatusesInCountyToResubmitInChronologicalOrderByOldestFirst() {
//    Application newestApplication = Application.builder()
//        .id("1")
//        .completedAt(ZonedDateTime.now(UTC).truncatedTo(ChronoUnit.MILLIS))
//        .applicationData(new TestApplicationDataBuilder().base().build())
//        .county(Anoka)
//        .build();
//    Application middleApplication = Application.builder()
//        .id("2")
//        .completedAt(ZonedDateTime.now(UTC).truncatedTo(ChronoUnit.MILLIS).minusDays(14))
//        .applicationData(new TestApplicationDataBuilder().base().build())
//        .county(Anoka)
//        .build();
//    Application oldestApplication = Application.builder()
//        .id("3")
//        .completedAt(ZonedDateTime.now(UTC).truncatedTo(ChronoUnit.MILLIS).minusMonths(1))
//        .applicationData(new TestApplicationDataBuilder().base().build())
//        .county(Olmsted)
//        .build();
//    applicationRepository.save(newestApplication);
//    applicationRepository.save(middleApplication);
//    applicationRepository.save(oldestApplication);
//
//    assertThat(applicationRepository.findApplicationsWithBlankStatuses(Anoka)
//        .stream().map(Application::getId))
//        .containsExactly("2", "1");
//  }
//
//  @Test
//  void shouldReturnAppsWithNoStatusesToResubmitInChronologicalOrderByOldestFirstAndCounty() {
//    Application wrightApp = Application.builder()
//        .id("1")
//        .completedAt(ZonedDateTime.now(UTC).truncatedTo(ChronoUnit.MILLIS))
//        .applicationData(new TestApplicationDataBuilder().base().build())
//        .county(Wright)
//        .build();
//    Application newerAnokaApp = Application.builder()
//        .id("3")
//        .completedAt(ZonedDateTime.now(UTC).truncatedTo(ChronoUnit.MILLIS).minusDays(1))
//        .applicationData(new TestApplicationDataBuilder().base().build())
//        .county(Anoka)
//        .build();
//    Application beltramiApp = Application.builder()
//        .id("5")
//        .completedAt(ZonedDateTime.now(UTC).truncatedTo(ChronoUnit.MILLIS).minusDays(14))
//        .applicationData(new TestApplicationDataBuilder().base().build())
//        .county(Beltrami)
//        .build();
//    Application oldAnokaApp = Application.builder()
//        .id("7")
//        .completedAt(ZonedDateTime.now(UTC).truncatedTo(ChronoUnit.MILLIS).minusMonths(1))
//        .applicationData(new TestApplicationDataBuilder().base().build())
//        .county(Anoka)
//        .build();
//
//    List.of(wrightApp, newerAnokaApp, beltramiApp, oldAnokaApp)
//        .forEach(application -> applicationRepository.save(application));
//
//    assertThat(applicationRepository.findApplicationsWithBlankStatuses().stream().map(Application::getId)).containsExactly(
//      oldAnokaApp.getId(), newerAnokaApp.getId(), beltramiApp.getId(), wrightApp.getId()
//    );
//  }
}
