package oit.is.z3488.kaizi.janken.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3488.kaizi.janken.model.Match;
import oit.is.z3488.kaizi.janken.model.MatchMapper;

@Service
public class AsyncKekka {

  @Autowired
  MatchMapper matchMapper;

  @Async
  public void pushResult(SseEmitter emitter, int myId) throws IOException {
    try {
      while (true) {
        TimeUnit.SECONDS.sleep(1);
        ArrayList<Match> list = matchMapper.selectActiveMatches();
        for (Match m : list) {
          if (m.getUser1() == myId || m.getUser2() == myId) {
            String text = "対戦結果: " + m.getUser1Hand() + " vs " + m.getUser2Hand()
                + " <a href=\"/janken\">[戻る]</a>";
            emitter.send(text);
            emitter.complete();
            return;
          }
        }
      }
    } catch (InterruptedException e) {
      emitter.complete();
    }
  }
}
