package org.codeforamerica.formflowstarter.app.flows;

import javax.validation.constraints.NotBlank;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestFlow {

  @NotBlank(message = "{validations.make-sure-to-provide-a-first-name}")
  String firstName;
}
