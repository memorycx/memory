package com.example.memory.Service;

import com.example.memory.mapper.UserMapper;

import com.example.memory.mapper.WordMapper;
import com.example.memory.pojo.User;
import com.example.memory.pojo.UserLearn;
import com.example.memory.pojo.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    WordService wordService;

    @Autowired
    WordMapper wordMapper;

    /**
     * 用户登录核验
     * 返回状态为 1 ，用户登录失败
     * 返回状态为 0 ，用户登录成功
     */
    public int login(String username,String password){
        User user = userMapper.searchUser(username);
        if(user == null) return 1;
        if(user.getUsername() == null)  return 1;
        if(user.getPwd() == null) return 1;
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        if(encoder.matches(password,user.getPwd())) return 0;
        return 1;
    }

    /**
     * 用户注册
     * 返回状态为 0 ，用户注册成功
     */
    public int register(User user) {

        // 进行密码的加密
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String pwd = encoder.encode(user.getPwd());
        user.setPwd(pwd);

        if(user.getUsername() == null || user.getUsername().isEmpty()) return 4;

        // 分别检测用户名和邮箱有没有重复
        if(userMapper.searchUser(user.getUsername()) != null) return 1;
        if((userMapper.searchEmail(user.getEmail())) != null) return 2;
        if(userMapper.addUser(user.getUsername(),user.getEmail(),user.getPwd()) == 0) return 3;

        return 0;
    }

    /**
     * 获取用户信息
     */
    public User userInfo(String username){
        return userMapper.searchUserInfo(username);
    }

    /**
     * 获取用户单背诵书籍的单词数量
     */
    public int wordNumber(String username){
        return wordService.searchBookInfo(username);
    }


    /*
     * 获取用户最近学习记录
     */
    public List<Map<Object, Object>> learn(String username) {
        List<UserLearn> tmeList = userMapper.learn(username);
        List<Map<Object, Object>> ansList = new ArrayList<>();
        for(UserLearn learn:tmeList){
            Map<Object, Object> map = new HashMap<>();
            // 只添加值不为0的字段
            map.put("date", learn.getDate());
            map.put("username", learn.getUsername());
            if (learn.getNewLearn() > 0) {
                map.put("new_learn", learn.getNewLearn());
            }
            if (learn.getReview() > 0) {
                map.put("review", learn.getReview());
            }
            if (learn.getRead() > 0) {
                map.put("read", learn.getRead());
            }
            if (learn.getSpeek() > 0) {
                map.put("speek", learn.getSpeek());
            }
            ansList.add(map);
        }
        return ansList;
    }

    public int continueLearn(String username) {
        return userMapper.searchUser(username).getContinueDay();
    }

    public void changeCurrentBook(String username,Integer bookId) {
        userMapper.changeCurrentBook(username,bookId);
    }

    public void updateWordStatus(Word word,String username) {
        // 首先判断是什么书籍

        int bookId = wordMapper.getCurrentBookId(username);
        int state = wordMapper.getBookState(bookId);

        if(state == 0){
            //走两个逻辑
            // 1.拿到单词的sate,然后更新用户今日学习表中的数据
            // 2.更新单词的学习状态

            // 补充：判断单词是否掌握，如果掌握，要维护用户表
            if(word.getState() == 0){
                userMapper.addWordStatus(word,username);
                userMapper.updateNewLearnToday(username);
            }else if(word.getState() < 4){
                userMapper.updateWordStatus(word,username);
                userMapper.updateReviewToday(username);
            }else if(word.getState() == 4){
                userMapper.updateReviewToday(username);
                userMapper.updateWordNums(username);
            }

        }else{
            userMapper.updateWordStatusBySelf(word.getWord(),username);
            if(word.getState() == 0){
                userMapper.updateNewLearnToday(username);
            }else if(word.getState() < 4){
                userMapper.updateReviewToday(username);
            }else if(word.getState() == 4){
                userMapper.updateReviewToday(username);
                userMapper.updateWordNums(username);
            }

        }



    }

    public void updateUser(User user, String username) {
        userMapper.updateUser(user,username);
    }
}
