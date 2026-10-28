package oit.is.z3488.kaizi.janken.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3488.kaizi.janken.model.Entry;
import oit.is.z3488.kaizi.janken.model.Janken;
import oit.is.z3488.kaizi.janken.model.User;
import oit.is.z3488.kaizi.janken.model.UserMapper;

import oit.is.z3488.kaizi.janken.model.Match;
import oit.is.z3488.kaizi.janken.model.MatchMapper;
import oit.is.z3488.kaizi.janken.model.MatchInfo;
import oit.is.z3488.kaizi.janken.model.MatchInfoMapper;
import oit.is.z3488.kaizi.janken.service.AsyncKekka;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @Autowired
  UserMapper userMapper;

  @Autowired
  MatchMapper matchMapper;

  @Autowired
  MatchInfoMapper matchInfoMapper;

  @Autowired
  AsyncKekka asyncKekka;

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

    ArrayList<MatchInfo> activeMatches = matchInfoMapper.selectAllActive();
    model.addAttribute("activeMatches", activeMatches);

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

  @GetMapping("/fight")
  public String fight(@RequestParam String hand, ModelMap model, Principal prin) {
    String loginUser = prin.getName();
    User me = userMapper.selectByName(loginUser);
    User cpu = userMapper.selectByName("CPU");

    Janken janken = new Janken();
    String cpuHand = janken.getCpuHand();

    Match match = new Match();
    match.setUser1(me.getId());
    match.setUser2(cpu.getId());
    match.setUser1Hand(hand);
    match.setUser2Hand(cpuHand);
    matchMapper.insertMatch(match);

    model.addAttribute("loginUser", loginUser);
    model.addAttribute("opponent", cpu);
    model.addAttribute("userHand", hand);
    model.addAttribute("cpuHand", cpuHand);
    model.addAttribute("result", janken.judge(hand));
    return "match.html";
  }

  @GetMapping("/wait")
  @Transactional
  public String waitPage(@RequestParam Integer id, @RequestParam String hand, ModelMap model, Principal prin) {
    String loginUser = prin.getName();
    User me = userMapper.selectByName(loginUser);

    // 自分を対戦相手として待機している(isActive=TRUE)レコードを探す
    MatchInfo waiting = matchInfoMapper.selectActiveByUser2(me.getId());

    if (waiting == null) {
      // 先手: matchinfoに登録して待機
      MatchInfo info = new MatchInfo();
      info.setUser1(me.getId());
      info.setUser2(id);
      info.setUser1Hand(hand);
      info.setActive(true);
      matchInfoMapper.insertMatchInfo(info);
    } else {
      // 後手: 試合成立。matchesに保存し、matchinfoを非アクティブにする
      Match match = new Match();
      match.setUser1(waiting.getUser1());
      match.setUser2(me.getId());
      match.setUser1Hand(waiting.getUser1Hand());
      match.setUser2Hand(hand);
      match.setActive(true);
      matchMapper.insertMatch(match);
      matchInfoMapper.updateDeactivate(waiting.getId());
    }

    model.addAttribute("loginUser", loginUser);
    return "wait.html";
  }

  @GetMapping("/result")
  public SseEmitter result(Principal prin) {
    final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    User me = userMapper.selectByName(prin.getName());
    try {
      this.asyncKekka.pushResult(emitter, me.getId());
    } catch (IOException e) {
      emitter.complete();
    }
    return emitter;
  }
}
