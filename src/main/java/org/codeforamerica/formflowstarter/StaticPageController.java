package org.codeforamerica.formflowstarter;


import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPageController {

  @GetMapping("/")
  String getIndex(
      HttpSession httpSession
  ) {
    httpSession.invalidate();

    return "index";
  }

  @GetMapping("/faq")
  String getFaq() {
    return "faq";
  }

}
