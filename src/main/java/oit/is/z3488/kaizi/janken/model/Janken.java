package oit.is.z3488.kaizi.janken.model;

public class Janken {

  private String cpuHand = "Gu";

  public String getCpuHand() {
    return cpuHand;
  }

  public String judge(String userHand) {
    if (userHand.equals(cpuHand)) {
      return "あいこ";
    }
    if (userHand.equals("Gu") && cpuHand.equals("Choki")
        || userHand.equals("Choki") && cpuHand.equals("Pa")
        || userHand.equals("Pa") && cpuHand.equals("Gu")) {
      return "勝ち";
    }
    return "負け";
  }
}
