package org.codeforamerica.formflowstarter.app.repository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.UUID;
import org.codeforamerica.formflowstarter.testutilities.AbstractMockMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest(properties = {"flowsConfig=flows-config/test-inputs.yaml"})
//@RunWith(PowerMockRunner.class)
public class SubflowDataTest extends AbstractMockMvcTest {


	@Override
	@BeforeEach
	protected void setUp() throws Exception {
		super.setUp();
	}
	@Test
	void shouldCallRemoveIncompleteIterationsWhenPostingToSubflowUuidEndpoint() throws Exception {
		UUID testUuid = UUID.randomUUID();
		PowerMockito.mockStatic(UUID.class);
		PowerMockito.when(UUID.randomUUID()).thenReturn(testUuid);
		postExpectingSuccess("inputs");
		mockMvc.perform(post("/testFlow/testIterationStartScreen/new").param("firstName", "Testy"));
		mockMvc.perform(post("/testFlow/middleScreen/" + testUuid).param("textInput", "foo"));
	}
}
