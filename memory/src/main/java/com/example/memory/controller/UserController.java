package com.example.memory.controller;

import com.example.memory.Service.UserService;
import com.example.memory.Tool.Identity;
import com.example.memory.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用户设置目标
     * newLearnPlane 新的学习计划
     * reviewPlane 新的复习计划
     */
    @PostMapping("/user/target")
    public void userTarget(@RequestBody User user, HttpServletRequest request){
        Identity.getUsername(user,request);
        userService.setTarget(user);
    }

    /**
     * 量化用户学习数据
     *  dayLearn 今日学习
     *  dayReview 今日复习
     *  dayRead 今日阅读
     */
    @PostMapping("/user/dayLearn")
    public void dayLearn(@RequestBody User user, HttpServletRequest request){
        Identity.getUsername(user,request);
        userService.dayLearn(user);
    }


}
