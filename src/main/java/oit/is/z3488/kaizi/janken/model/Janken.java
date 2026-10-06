package oit.is.z3488.kaizi.janken.model;

public class Janken {

  private String cpuHand = "ぐー";

  public String getCpuHand() {
    return cpuHand;
  }

  public String judge(String userHand) {
    if (userHand.equals(cpuHand)) {
      return "あいこ";
    }
    if (userHand.equals("ぐー") && cpuHand.equals("ちょき")
        || userHand.equals("ちょき") && cpuHand.equals("ぱー")
        || userHand.equals("ぱー") && cpuHand.equals("ぐー")) {
      return "勝ち";
    }
    return "負け";
  }
}
