package org.codeforamerica.formflowstarter;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

  @GetMapping("/")
  String getRoot() {
    return "index";
  }

  @GetMapping("/faq")
  String getFaq() { return "faq"; }

}
