package org.codeforamerica.formflowstarter.app.flows;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestFlow {

  @NotBlank(message = "{validations.make-sure-to-provide-a-first-name}")
//  @Size(min = 2, message = "{validations.test}")
  String firstName;
}
