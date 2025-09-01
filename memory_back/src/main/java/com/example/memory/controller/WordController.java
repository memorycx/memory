package com.example.memory.controller;


import com.example.memory.Service.WordService;
import com.example.memory.pojo.Book;
import com.example.memory.pojo.Result;
import com.example.memory.pojo.Word;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WordController {
    @Autowired
    private WordService wordService;

    /**
     * 这里使用的是简单参数传递
     * @param id 词汇书的id
     * @return 本书的所有词汇
     */
    @GetMapping("/word")
    public Result getWord(Integer id){
        List<Word> words = wordService.getWord(id);
        return Result.success(words);
    }

    /**
     * 获取用户今天需要背诵的单词
     * 1.得到该用户的学习计划
     * 2.用户当前学习书籍
     *   2.1 得到该书籍的所有单词
     *   2.2 与已经背的单词进行对比
     * 3.返回该用户需要学习的单词
     */
    @GetMapping("/word/today")
    public Result today(HttpServletRequest request){
        String username = request.getAttribute("username").toString();
        List<Word> words = wordService.getTodayWord(username);
        return Result.success(words);
    }


    @GetMapping("/word/review")
    public Result review(HttpServletRequest request){
        String username = request.getAttribute("username").toString();
        List<Word> words = wordService.getReviewWord(username);
        return Result.success(words);
    }


    // 目前还剩两个接口没有写
    // 1. 用户今日背的单词 ---> 添加到用户单词状态表
    // 2. 今日复习的的单词 ---> 更新用户的单词状态表

    @PostMapping("/word/learned")
    public Result learn(@RequestBody List<Integer> wordList, HttpServletRequest request){
        String username = request.getAttribute("username").toString();
        wordService.learn(wordList,username);
        return Result.success();
    }

    @PostMapping("/word/reviewed")
    public Result review(@RequestBody List<Integer> wordList, HttpServletRequest request){
        String username = request.getAttribute("username").toString();
        wordService.reviewed(wordList,username);
        return Result.success();
    }






}
