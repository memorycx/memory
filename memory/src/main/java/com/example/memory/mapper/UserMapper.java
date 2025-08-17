package com.example.memory.mapper;


import com.example.memory.pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;

@Mapper
public interface UserMapper {


    /**
     * 用户登录核验
     */
    @Select("SELECT username FROM users WHERE username = #{username}")
    String searchUserName(String username);

    @Select("SELECT pwd FROM users WHERE username = #{username}")
    String searchUserPwd(String username);

    @Select("SELECT username FROM users WHERE email = #{email}")
    User searchEmail(String email);


    /**
     * 用户注册
     */
    @Insert("INSERT INTO users (username, email, pwd) VALUES (#{username}, #{email}, #{pwd})")
    int addUser(String username, String email, String pwd);

    /**
     * 设置用户学习目标
     */
    @Update("UPDATE users SET new_learn_plane = #{newLearnPlane}, review_plane = #{reviewPlane} WHERE username = #{username}")
    void setTarget(User user);


    /**
     * 维护用户每日学习数据
     */

    @Select("SELECT * FROM user_day_num WHERE username = #{username} AND day = #{day}")
    User isLearn(LocalDate day, String username);


    @Insert("INSERT INTO user_day_num (username, day, day_learn, day_review, day_read) \n" +
            "VALUES (#{user.username}, #{day}, #{user.dayLearn}, #{user.dayReview}, #{user.dayRead})")
    void dayLearn(LocalDate day, User user);


    @Update("UPDATE user_day_num SET day_learn = #{user.dayLearn}, day_review = #{user.dayReview}, day_read = #{user.dayRead} WHERE username = #{user.username} AND day = #{day}")
    void updateDayLearn(LocalDate day, User user);
}
