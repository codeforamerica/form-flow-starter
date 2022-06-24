package org.codeforamerica.formflowstarter.testutilities;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.codeforamerica.formflowstarter.app.data.Submission;
import org.codeforamerica.formflowstarter.app.data.SubmissionRepositoryService;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
//@Tag("db")
@JdbcTest
@AutoConfigureTestDatabase(replace = NONE)
@Sql(statements = {"ALTER SEQUENCE id RESTART WITH 12", "TRUNCATE TABLE submissions"})
@ContextConfiguration(classes = {
    Submission.class,
    SubmissionRepositoryService.class})
@AutoConfigureJson
public class AbstractSubmissionRepositoryTest {

}
