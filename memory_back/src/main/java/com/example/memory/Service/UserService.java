package com.example.memory.Service;

import com.example.memory.mapper.UserMapper;

import com.example.memory.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public int login(String username,String password){
        if(userMapper.searchUserName(username) == null){
            return 0;
        }
        String pwd = userMapper.searchUserPwd(username);
        if(pwd == null){
            return 0;
        }
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        if(encoder.matches(password,pwd)){
            return 1;
        }
        return 0;
    }

    public int register(User user) {

        // 进行密码的加密
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String pwd = encoder.encode(user.getPwd());

        String username = user.getUsername();
        String email = user.getEmail();

        // 分别检测用户名和邮箱有没有重复
        if(userMapper.searchUserName(username) != null){
            return 1;
        }
        if(userMapper.searchEmail(email)  != null){
            return 2;
        }

        if(userMapper.addUser(username,email,pwd) == 0){
            return 3;
        }
        return 0;
    }

    public void setTarget(User user) {
        userMapper.setTarget(user);
    }


    /*
    首先查询今日有没有学习记录
    如果有，那就更新数据库
    如果没有，那就插入数据库
     */
    public void dayLearn(User user) {
        LocalDate today = LocalDate.now();
        User user1 = userMapper.isLearn(today,user.getUsername());
        if(user1 == null){
            userMapper.dayLearn(today,user);
        }else{
            userMapper.updateDayLearn(today,user);
        }
    }
}
