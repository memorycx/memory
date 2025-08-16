package com.example.memory.Controller;

import com.example.memory.Service.UserService;
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

    @PostMapping("/user/target")
    public void userTarget(@RequestBody User user, HttpServletRequest request){
        String username = request.getAttribute("username").toString();
        System.out.println(username);
        user.setUsername(username);
        userService.setTarget(user);

    }
}
