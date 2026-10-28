package oit.is.z3488.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchMapper {

  @Select("SELECT id, user1, user2, user1Hand, user2Hand, isActive AS active from matches")
  ArrayList<Match> selectAllMatches();

  @Select("SELECT id, user1, user2, user1Hand, user2Hand, isActive AS active from matches where isActive = TRUE")
  ArrayList<Match> selectActiveMatches();

  @Insert("INSERT INTO matches (user1, user2, user1Hand, user2Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{user2Hand}, #{active});")
  void insertMatch(Match match);

  @Update("UPDATE matches SET isActive = FALSE WHERE id = #{id}")
  void updateDeactivate(int id);

}
