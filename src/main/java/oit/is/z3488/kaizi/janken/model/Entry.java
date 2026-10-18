package oit.is.z3488.kaizi.janken.model;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class Entry {

  private ArrayList<String> users = new ArrayList<>();

  public void addUser(String user) {
    if (!users.contains(user)) {
      users.add(user);
    }
  }

  public ArrayList<String> getUsers() {
    return users;
  }
}
