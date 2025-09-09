package com.example.memory.Service;

import com.example.memory.mapper.UserMapper;

import com.example.memory.pojo.Book;
import com.example.memory.pojo.User;
import com.example.memory.pojo.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    WordService wordService;

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
//    public List<UserLearn> learn(String username) {
//        return userMapper.learn(username);
//    }

    public int continueLearn(String username) {
        return userMapper.searchUser(username).getContinueDay();
    }

    public void changeCurrentBook(String username,Integer bookId) {
        userMapper.changeCurrentBook(username,bookId);
    }

    public void updateWordStatus(Word word,String username) {
        //走两个逻辑
        // 1.拿到单词的sate,然后更新用户今日学习表中的数据
        // 2.更新单词的学习状态
        if(word.getState() == 0){
            userMapper.addWordStatus(word,username);
            userMapper.updateNewLearnToday(username);
        }else{
            userMapper.updateWordStatus(word,username);
            userMapper.updateReviewToday(username);
        }


    }
}
