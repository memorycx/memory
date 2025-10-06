package com.example.memory.mapper;


import com.example.memory.pojo.User;
import com.example.memory.pojo.UserLearn;
import com.example.memory.pojo.Word;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {


    /**
     * 用户登录核验
     */
    @Select("SELECT username,pwd,email,continue_day FROM users WHERE username = #{username}")
    User searchUser(String username);


    /**
     * 用户注册
     */
    @Insert("INSERT INTO users (username, email, pwd) VALUES (#{username}, #{email}, #{pwd})")
    int addUser(String username, String email, String pwd);

    @Select("SELECT * FROM users WHERE email = #{email}")
    Integer searchEmail(String email);


    /**
     * 获取用户信息
     */
    @Select("SELECT username,email,gender,address,school,new_learn_plane,review_plane,current_book_id,text_nums,word_nums,speek_nums FROM users WHERE username = #{username}")
    User searchUserInfo(String username);


    /**
     * 获取用户最近学习记录
     */
    @Select("SELECT `date`,username,new_learn,review,`read`,speek FROM user_day_num WHERE username = #{username} ORDER BY date DESC LIMIT 10")
    List<UserLearn> learn(String username);

    /**
     * 更改用户目前在背的词汇书
     */
    @Update("UPDATE users SET current_book_id = #{bookId} WHERE username = #{username}")
    void changeCurrentBook(String username,Integer bookId);



    /**
     * 添加单词状态
     */
    @Insert("INSERT into user_word_state(username,word_id,update_time,state)" +
            "VALUES (#{username},#{word.wordId},now(),#{word.state} + 1)")
    void addWordStatus(Word word, String username);

    /**
     * 更新单词状态
     */
    @Update("UPDATE user_word_state SET state = #{word.state} + 1,update_time = now() WHERE username = #{username} AND word_id = #{word.wordId}")
    void updateWordStatus(Word word,String username);


    void updateNewLearnToday(String username);

    void updateReviewToday(String username);

    void updateReadToday(String username);

    @Update("update users set word_nums = word_nums + 1 where username = #{username}")
    void updateWordNums(String username);

    @Update("update users set text_nums = text_nums + 1 where username = #{username}")
    void updateReadNum(String username);

    @Update("update users set gender = #{user.gender},address = #{user.address},school = #{user.school},new_learn_plane = #{user.newLearnPlane} where username = #{username}")
    void updateUser(User user, String username);


    @Update("update user_book set state =  state + 1 where username = #{username} and word = #{word}")
    void updateWordStatusBySelf(String word, String username);
}
