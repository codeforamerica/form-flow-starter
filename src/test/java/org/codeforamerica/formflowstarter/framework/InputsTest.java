package org.codeforamerica.formflowstarter.framework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.codeforamerica.formflowstarter.testutilities.AbstractMockMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"flowsConfig=flows-config/test-inputs.yaml"})
//@DirtiesContext()
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
    List<String> checkboxSet = List.of("Checkbox A", "Checkbox B");
    String checkboxInput = "checkbox value";
    String radioInput = "Radio B";
    String selectInput = "Select B";
    String moneyInput = "100";
    String phoneInput = "(555) 555-1234";
    String ssnInput = "333-22-4444";

    postExpectingNextPageTitle("inputs",
        Map.ofEntries(
            Map.entry("textInput", List.of(textInput)),
            Map.entry("areaInput", List.of(areaInput)),
            Map.entry("dateMonth", List.of(dateMonth)),
            Map.entry("dateDay", List.of(dateDay)),
            Map.entry("dateYear", List.of(dateYear)),
            Map.entry("numberInput", List.of(numberInput)),
            // CheckboxSet's need to have the [] in their name for POST actions
            Map.entry("checkboxSet[]", checkboxSet),
            Map.entry("checkboxInput", List.of(checkboxInput)),
            Map.entry("radioInput", List.of(radioInput)),
            Map.entry("selectInput", List.of(selectInput)),
            Map.entry("moneyInput", List.of(moneyInput)),
            Map.entry("phoneInput", List.of(phoneInput)),
            Map.entry("ssnInput", List.of(ssnInput))),
        "Housemate Info");

    var inputsScreen = goBackTo("inputs");

    assertThat(inputsScreen.getInputValue("textInput")).isEqualTo(textInput);
    assertThat(inputsScreen.getTextAreaAreaValue("areaInput")).isEqualTo(areaInput);
    assertThat(inputsScreen.getInputValue("dateMonth")).isEqualTo(dateMonth);
    assertThat(inputsScreen.getInputValue("dateDay")).isEqualTo(dateDay);
    assertThat(inputsScreen.getInputValue("dateYear")).isEqualTo(dateYear);
    assertThat(inputsScreen.getInputValue("numberInput")).isEqualTo(numberInput);
    assertThat(inputsScreen.getCheckboxSetValues("checkboxSet")).isEqualTo(checkboxSet);
    assertThat(inputsScreen.getCheckboxValue("checkboxInput")).isEqualTo(checkboxInput);
    assertThat(inputsScreen.getRadioValue("radioInput")).isEqualTo(radioInput);
    assertThat(inputsScreen.getSelectValue("selectInput")).isEqualTo(selectInput);
    assertThat(inputsScreen.getInputValue("moneyInput")).isEqualTo(moneyInput);
    assertThat(inputsScreen.getInputValue("phoneInput")).isEqualTo(phoneInput);
    assertThat(inputsScreen.getInputValue("ssnInput")).isEqualTo(ssnInput);
  }

//  TODO: Should data persist still? I don't think so after today's discussion
//  @Test
//  void shouldPersistSubworkflowInputs() throws Exception {
//    String housemateFirstName = "John";
//    String housemateLastName = "Perez";
//    String householdMemberRelationship = "Spouse";
//    // post to that screen
//    postExpectingNextPageTitle("housemateInfo",
//      Map.of(
//        "householdMemberFirstName", List.of(housemateFirstName),
//        "householdMemberLastName", List.of(housemateLastName),
//        "householdMemberRelationship", List.of(householdMemberRelationship),
//        "householdMemberRecentlyMovedToUS", List.of("No")),
//          "Household List");
//
//    // verify that the values are persisted
//    var housemateInfoScreen = goBackTo("housemateInfo");
//    assertThat(housemateInfoScreen.getInputValue("householdMemberFirstName")).isEqualTo(housemateFirstName);
//    assertThat(housemateInfoScreen.getInputValue("householdMemberLastName")).isEqualTo(housemateLastName);
//    assertThat(housemateInfoScreen.getSelectValue("householdMemberRelationship")).isEqualTo(householdMemberRelationship);
//    assertThat(housemateInfoScreen.getRadioValue("recentlyMovedToUS")).isEqualTo("No");
//  }
}
