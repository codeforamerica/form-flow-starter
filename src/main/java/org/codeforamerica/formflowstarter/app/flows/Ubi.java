package org.codeforamerica.formflowstarter.app.flows;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class Ubi {

  @NotBlank(message = "{validation.make-sure-to-provide-a-first-name}")
  String firstName;

  @NotBlank
  String lastName;

  @Email
  String emailAddress;

  String phoneNumber;
}
