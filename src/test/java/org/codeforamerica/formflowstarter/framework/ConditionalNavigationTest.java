package org.codeforamerica.formflowstarter.framework;

import org.codeforamerica.formflowstarter.testutilities.AbstractMockMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"flowsConfig=flows-config/test-conditional-navigation.yaml"})
public class ConditionalNavigationTest extends AbstractMockMvcTest {

  @Test
  void shouldGoToPageWhoseConditionIsSatisfied() throws Exception {
    postExpectingNextPageTitle("first", "firstName", "Testy McTesterson", "Second Page");
  }

  @Test
  void shouldNotGoToPageWhoseConditionIsNotSatisfied() throws Exception {
    postExpectingNextPageTitle("first", "firstName", "Not Testy McTesterson", "Other Page");
  }

  @Test
  void shouldGoToTheFirstPageInNextPagesIfThereAreTwoPagesWithNoConditions() throws Exception {
    continueExpectingNextPageTitle("second", "Other Page");
  }
}
