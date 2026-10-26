package oit.is.z3488.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3488.kaizi.janken.model.Entry;
import oit.is.z3488.kaizi.janken.model.Janken;
import oit.is.z3488.kaizi.janken.model.User;
import oit.is.z3488.kaizi.janken.model.UserMapper;

import oit.is.z3488.kaizi.janken.model.Match;
import oit.is.z3488.kaizi.janken.model.MatchMapper;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @Autowired
  UserMapper userMapper;

  @Autowired
  MatchMapper matchMapper;

  @GetMapping("/janken")
  public String jankenGet(@RequestParam(required = false) String hand, ModelMap model, Principal prin) {
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("entry", this.entry);

    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

    if (hand != null) {
      Janken janken = new Janken();
      model.addAttribute("userHand", hand);
      model.addAttribute("cpuHand", janken.getCpuHand());
      model.addAttribute("result", janken.judge(hand));
    }
    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam Integer id, ModelMap model, Principal prin) {
    String loginUser = prin.getName();
    User opponent = userMapper.selectById(id);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("opponent", opponent);
    return "match.html";
  }
}
