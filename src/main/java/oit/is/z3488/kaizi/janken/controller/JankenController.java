package oit.is.z3488.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3488.kaizi.janken.model.Janken;

@Controller
public class JankenController {

  @GetMapping("/janken")
  public String jankenGet(@RequestParam(required = false) String hand, ModelMap model) {
    if (hand != null) {
      Janken janken = new Janken();
      model.addAttribute("userHand", hand);
      model.addAttribute("cpuHand", janken.getCpuHand());
      model.addAttribute("result", janken.judge(hand));
    }
    return "janken.html";
  }

  @PostMapping("/janken")
  public String jankenPost(@RequestParam String userName, ModelMap model) {
    model.addAttribute("greeting", "Hi " + userName);
    return "janken.html";
  }
}
