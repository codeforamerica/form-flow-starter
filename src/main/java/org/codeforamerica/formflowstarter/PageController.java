package org.codeforamerica.formflowstarter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
  @GetMapping("/page")
  String getPage() {
    return "page";
  }
}
