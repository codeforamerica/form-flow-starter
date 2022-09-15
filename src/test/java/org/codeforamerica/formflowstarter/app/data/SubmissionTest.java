package org.codeforamerica.formflowstarter.app.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class SubmissionTest {

	@Test
	void shouldRemoveIncompleteSubflowIterations() {
		Submission submission = new Submission();
		HashMap<String, Object> inputData = new HashMap<>();
		ArrayList<HashMap<String, Object>> subflow = new ArrayList<>();
		HashMap<String, Object> completeIteration = new HashMap<>();
		HashMap<String, Object> incompleteIteration = new HashMap<>();
		HashMap<String, Object> currentButIncompleteIteration = new HashMap<>();
		completeIteration.put("uuid", "completeIterationUuid");
		completeIteration.put("iterationIsComplete", true);
		incompleteIteration.put("uuid", "incompleteIterationUuid");
		incompleteIteration.put("iterationIsComplete", false);
		currentButIncompleteIteration.put("uuid", "currentButIncompleteUuid");
		currentButIncompleteIteration.put("iterationIsComplete", false);
		subflow.add(completeIteration);
		subflow.add(incompleteIteration);
		subflow.add(currentButIncompleteIteration);
		inputData.put("testSubflow", subflow);
		submission.setInputData(inputData);
		var subflowArr = (ArrayList<HashMap<String, Object>>) submission.getInputData().get("testSubflow");
		assertThat(subflowArr.size()).isEqualTo(3);
		Submission.removeIncompleteIterations(submission, "testSubflow", "currentButIncompleteUuid");
		assertThat(subflowArr.size()).isEqualTo(2);
		assertThat(subflowArr.get(0).get("iterationIsComplete")).isEqualTo(true);
		assertThat(subflowArr.get(1).get("iterationIsComplete")).isEqualTo(false);
	}
}
