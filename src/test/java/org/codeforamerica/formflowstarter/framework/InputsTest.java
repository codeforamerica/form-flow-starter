package org.codeforamerica.formflowstarter.framework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.codeforamerica.formflowstarter.testutilities.AbstractMockMvcTest;
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
  void shouldPersistInputValuesWhenNavigatingBetweenScreens() throws Exception {
    String textInput = "foo";
    String areaInput = "foo bar baz";
    String dateMonth = "10";
    String dateDay = "30";
    String dateYear = "2020";
    String numberInput = "123";
    List<String> checkboxSet = List.of("A", "B");
    String checkboxInput = "1";

    postExpectingNextPageTitle("inputs",
        Map.of("textInput", List.of(textInput),
               "areaInput", List.of(areaInput),
               "dateMonth", List.of(dateMonth),
               "dateDay", List.of(dateDay),
               "dateYear", List.of(dateYear),
               "numberInput", List.of(numberInput),
               "checkboxSet", checkboxSet,
               "checkboxInput", List.of(checkboxInput)),
        "Success");

    var inputsScreen = goBackTo("inputs");

    assertThat(inputsScreen.getInputValue("textInput")).isEqualTo(textInput);
    assertThat(inputsScreen.getTextAreaAreaValue("areaInput")).isEqualTo(areaInput);
    assertThat(inputsScreen.getInputValue("dateMonth")).isEqualTo(dateMonth);
    assertThat(inputsScreen.getInputValue("dateDay")).isEqualTo(dateDay);
    assertThat(inputsScreen.getInputValue("dateYear")).isEqualTo(dateYear);
    assertThat(inputsScreen.getInputValue("numberInput")).isEqualTo(numberInput);
    assertThat(inputsScreen.getCheckboxSetValues("checkboxSet")).isEqualTo(checkboxSet);
    assertThat(inputsScreen.getCheckboxValue("checkboxInput")).isEqualTo(checkboxInput);

  }
}
