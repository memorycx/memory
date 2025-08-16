package com.example.memory.Controller;
import com.example.memory.Service.UserService;
import com.example.memory.Tool.JwtUtils;
import com.example.memory.pojo.Result;

import com.example.memory.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    @Autowired
    UserService userService;


    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        int state = userService.login(user.getUsername(),user.getPwd());
        if(state == 1){
            String token = JwtUtils.generateToken(user.getUsername());
            return Result.success(token);
        }
        return Result.error("登录失败");
    }

    /**
     * 注册封装参数有以下：
     * 用户名，密码，邮箱
     * 业务逻辑：
     * 1.核验用户名和邮箱是否存在
     * 2.加密密码
     * 3.注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user){

        int i = userService.register(user);
        if(i==0){
            return Result.success("注册成功");
        }
        if(i==1){
            return Result.error("用户名已存在");
        }
        if(i==2){
            return Result.error("该邮箱已绑定");
        }
        if(i==3){
            return Result.error("奇怪的错误，请再试一次");
        }
        return Result.error("注册失败");
    }
}
