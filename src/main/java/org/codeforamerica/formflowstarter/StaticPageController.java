package org.codeforamerica.formflowstarter;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPageController {

  @GetMapping("/")
  String getIndex() { return "index"; }

  @GetMapping("/faq")
  String getFaq() { return "faq"; }

}
