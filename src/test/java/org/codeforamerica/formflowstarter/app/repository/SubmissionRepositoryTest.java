package org.codeforamerica.formflowstarter.app.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
// TODO: rework to work with h2
//@Sql(statements = {"ALTER SEQUENCE submissions_id_seq RESTART WITH 12", "TRUNCATE TABLE submissions"})
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
    assertThat(firstSubmission.getId()).isEqualTo(2);
    assertThat(secondSubmission.getId()).isEqualTo(3);
  }

  @Test
  void shouldSaveSubmission() {
    var inputData = Map.of(
        "testKey", "this is a test value",
        "otherTestKey", List.of("A", "B", "C"),
        "household", List.of(Map.of("firstName", "John", "lastName", "Perez")));
    var timeNow = Instant.now();
    var submission = Submission.builder()
        .inputData(inputData)
        .flow("testFlow")
        .submittedAt(Date.from(timeNow))
        .build();

    submissionRepository.save(submission);

    Optional<Submission> savedSubmissionOptional = submissionRepository.findById(submission.getId());
    Submission savedSubmission = null;
    if (savedSubmissionOptional.isPresent()) {
      savedSubmission = savedSubmissionOptional.get();
    }
    assertThat(savedSubmission.getFlow()).isEqualTo("testFlow");
    assertThat(savedSubmission.getInputData()).isEqualTo(inputData);
    assertThat(new SimpleDateFormat("YYYY-mm-dd HH:mm:ss").format(savedSubmission.getSubmittedAt()))
        .isEqualTo(new SimpleDateFormat("YYYY-mm-dd HH:mm:ss").format(Timestamp.from(timeNow)));
  }

  @Test
  void shouldUpdateExistingApplication() {
    var inputData = Map.of(
        "testKey", "this is a test value",
        "otherTestKey", List.of("A", "B", "C"));
    var timeNow = Instant.now();
    var submission = Submission.builder()
        .inputData(inputData)
        .flow("testFlow")
        .submittedAt(Date.from(timeNow))
        .build();
    submissionRepository.save(submission);

    var newInputData = Map.of(
        "newKey", "this is a new value",
        "otherNewKey", List.of("X", "Y", "Z"));
    submission.setInputData(newInputData);
    submissionRepository.save(submission);

    Optional<Submission> savedSubmissionOptional = submissionRepository.findById(submission.getId());
    Submission savedSubmission = null;
    if (savedSubmissionOptional.isPresent()) {
      savedSubmission = savedSubmissionOptional.get();
    }
    assertThat(savedSubmission.getInputData()).isEqualTo(newInputData);


  }
}
