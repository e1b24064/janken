package oit.is.z3488.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchInfoMapper {

  @Select("SELECT id, user1, user2, user1Hand, isActive AS active from matchinfo where isActive = TRUE")
  ArrayList<MatchInfo> selectAllActive();

  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{active});")
  void insertMatchInfo(MatchInfo matchInfo);

  @Select("SELECT id, user1, user2, user1Hand, isActive AS active from matchinfo where isActive = TRUE AND user2 = #{user2} ORDER BY id LIMIT 1")
  MatchInfo selectActiveByUser2(int user2);

  @Update("UPDATE matchinfo SET isActive = FALSE WHERE id = #{id}")
  void updateDeactivate(int id);
}
