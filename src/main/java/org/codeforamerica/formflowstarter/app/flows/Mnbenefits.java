package org.codeforamerica.formflowstarter.app.flows;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class Mnbenefits {
  @NotEmpty(message = "Please select the county you live in.")
  String county;

  String writtenLanguage;

  @NotBlank(message = "Please select a language you speak.")
  String spokenLanguage;

  @NotBlank(message = "Please select if you need an interpreter.")
  String interpreter;
}
