package com.example.memory.Mapper;


import com.example.memory.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUser(String username);

    @Select("SELECT username FROM users WHERE username = #{username}")
    String searchUserName(String username);

    @Select("SELECT pwd FROM users WHERE username = #{username}")
    String searchUserPwd(String username);

    @Select("SELECT username FROM users WHERE email = #{email}")
    User searchEmail(String email);


    @Insert("INSERT INTO users (username, email, pwd) VALUES (#{username}, #{email}, #{pwd})")
    int addUser(String username, String email, String pwd);


    @Update("UPDATE users SET new_learn_plane = #{newLearnPlane}, review_plane = #{reviewPlane} WHERE username = #{username}")
    void setTarget(User user);
}
