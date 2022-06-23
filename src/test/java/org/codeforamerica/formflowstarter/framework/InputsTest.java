package org.codeforamerica.formflowstarter.framework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.codeforamerica.formflowstarter.testutilities.AbstractMockMvcTest;
import org.codeforamerica.formflowstarter.testutilities.DatePart;
import org.codeforamerica.formflowstarter.testutilities.FormScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"flowsConfig=flows-config/test-inputs.yaml"})
public class InputsTest extends AbstractMockMvcTest {

  @Override
  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Test
  void shouldPersistInputValuesWhenNavigationBetweenPages() throws Exception {
    String dateDay = "30";
    String dateMonth = "10";
    String dateYear = "2020";
    // Post to inputs expecting to go to success
    postExpectingNextPageTitle("inputs",
        Map.of("textInput", List.of("foo"),
               "areaInput", List.of("foo bar baz"),
               "date", List.of(dateMonth, dateDay, dateYear)),
        "Success");
    // Go back
    var inputsPage = new FormScreen(getPage("inputs"));
    // Assert input values persist
    assertThat(inputsPage.getInputValue("textInput")).isEqualTo("foo");
    assertThat(inputsPage.getTextAreaAreaValue("areaInput")).isEqualTo("foo bar baz");
    assertThat(inputsPage.getDateValue("date", DatePart.DAY)).isEqualTo(dateDay);
    assertThat(inputsPage.getDateValue("date", DatePart.MONTH)).isEqualTo(dateMonth);
    assertThat(inputsPage.getDateValue("date", DatePart.YEAR)).isEqualTo(dateYear);
  }
}
