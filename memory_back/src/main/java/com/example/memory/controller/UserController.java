package com.example.memory.controller;

import com.example.memory.Service.UserService;

import com.example.memory.pojo.Result;
import com.example.memory.pojo.User;

import com.example.memory.pojo.Word;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 获取用户信息
     */
    @GetMapping ("api/user/info")
    public Result userInfo(HttpServletRequest request){
        String username = request.getAttribute("username").toString();
        User user = userService.userInfo(username);
        if(user == null){
            return Result.error("账号异常");
        }
        return Result.success(user);
    }
    /**
     * 获取用户单背诵书籍的单词数量
     */
    @GetMapping("api/user/wordNumber")
    public Result wordNumber(HttpServletRequest request){
        String username = request.getAttribute("username").toString();
        int wordNums = userService.wordNumber(username);
        return Result.success(wordNums);
    }

    /*
     获取用户最近学习记录
     */
//    @GetMapping("api/user/tableData")
//    public Result learn(HttpServletRequest request){
//        String username = request.getAttribute("username").toString();
//        List<UserLearn> userLearns = userService.learn(username);
//        if(userLearns == null){
//            return Result.error("暂无学习记录");
//        }
//        return Result.success(userLearns);
//    }

    /*
    获取用户连续学习天数
     */
    @GetMapping("api/user/day")
    public Result continueLearn(HttpServletRequest request){
        String username = request.getAttribute("username").toString();
        int continueLearn = userService.continueLearn(username);
        return Result.success(continueLearn);
    }

    @PostMapping("api/user/updateWordStatus")
    public Result updateWordStatus(@RequestBody Word word, HttpServletRequest request){
        log.info("{}",word.toString());
        String username = request.getAttribute("username").toString();
        userService.updateWordStatus(word,username);
        return Result.success("更改成功");
    }




}
