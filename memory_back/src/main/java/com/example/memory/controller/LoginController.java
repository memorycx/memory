package com.example.memory.controller;
import com.example.memory.Service.UserService;
import com.example.memory.Tool.JwtUtils;
import com.example.memory.pojo.Result;

import com.example.memory.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/api/login")
    public Result login(@RequestBody User user) {
        int state = userService.login(user.getUsername(),user.getPwd());
        if(state==1) return Result.error("登录失败");
        return Result.success(JwtUtils.generateToken(user.getUsername()));
    }

    @PostMapping("/api/register")
    public Result register(@RequestBody User user){
        int state = userService.register(user);
        if(state==0){
            return Result.success("注册成功");
        } else if (state==1) {
            return Result.error("用户名已存在");
        } else if (state==2) {
            return Result.error("邮箱已存在");
        } else if (state == 3) {
            return Result.error("服务端错误");
        }else if (state==4){
            return Result.error("用户名或密码为空");
        }
        return Result.error("注册失败");
    }



}
