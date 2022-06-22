package org.codeforamerica.formflowstarter.framework;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.codeforamerica.formflowstarter.app.config.ConditionHandler;
import org.codeforamerica.formflowstarter.testutilities.AbstractMockMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {"flowsConfig=flows-config/test-conditional-navigation.yaml"})
public class ConditionalNavigationTest extends AbstractMockMvcTest {
  @MockBean
  ConditionHandler mockedConditionHandler;

  @Test
  void shouldGoToPageWhoseConditionIsSatisfied() throws Exception {
    when(mockedConditionHandler.handleCondition(anyString())).thenReturn(true);
    postExpectingNextPageTitle("first", "firstName", "Testy McTesterson", "Second Page");
  }

  @Test
  void shouldNotGoToPageWhoseConditionIsNotSatisfied() throws Exception {
    when(mockedConditionHandler.handleCondition(anyString())).thenReturn(false);
    postExpectingNextPageTitle("first", "firstName", "Not Testy McTesterson", "Other Page");
  }

  @Test
  void shouldGoToTheFirstPageInNextPagesIfThereAreTwoPagesWithNoConditions() throws Exception {
    continueExpectingNextPageTitle("second", "Other Page");
  }
}
