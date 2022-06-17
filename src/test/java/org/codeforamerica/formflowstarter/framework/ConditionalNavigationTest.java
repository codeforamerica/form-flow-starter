package org.codeforamerica.formflowstarter.framework;

import org.codeforamerica.formflowstarter.testutilities.AbstractMockMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"flowsConfig=flows-config/test-conditional-navigation.yaml"})
public class ConditionalNavigationTest extends AbstractMockMvcTest {

  void shouldGoToPageWhoseConditionIsSatisfied() {

  }
}
