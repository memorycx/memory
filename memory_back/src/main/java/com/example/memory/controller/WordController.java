package com.example.memory.controller;


import com.example.memory.Service.UserService;
import com.example.memory.Service.WordService;
import com.example.memory.pojo.Book;
import com.example.memory.pojo.Result;
import com.example.memory.pojo.Word;
import com.example.memory.pojo.WordForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class WordController {
    @Autowired
    private WordService wordService;
    @Autowired
    private UserService userService;

    /**
     * 获取用户目前在背的词汇书的id
     */
    @GetMapping("/api/word/getCurrentBook")
    public Result getCurrentBook(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return Result.success(wordService.getCurrentBookId(username));
    }

    /**
     * 获取用户所有的词汇书
     */
    @GetMapping("/api/word/getBookList")
    public Result getBookList(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return Result.success(wordService.getBookList(username));
    }

    /**
     * 更改用户目前在背的词汇书
     */
    @PostMapping("/api/word/changeBook")
    public Result changeCurrentBook(HttpServletRequest request,@RequestBody Integer bookId) {
        String username = (String) request.getAttribute("username");
        userService.changeCurrentBook(username,bookId);
        return Result.success();
    }

    /*
      获取背诵和复习单词列表
     */
    @GetMapping("/api/word/getWordList")
    public Result getWordList(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return Result.success(wordService.getWordList(username));
    }

    /*
    获得今日已经背诵的单词数量
     */
    @GetMapping("api/user/getToday")
    public Result getNums(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return Result.success(wordService.getNums(username));
    }

    /*
    继续学习
     */
    @GetMapping("/api/word/continueLearn")
    public Result continueLearn(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return Result.success(wordService.getContinueLearnWord(username));
    }

    @GetMapping("/api/word/setKnow")
    public Result setKnow(HttpServletRequest request,
                          @RequestParam Integer wordId,
                          @RequestParam Integer state) {
        String username = (String) request.getAttribute("username");
        wordService.setKnow(wordId,state,username);
        return Result.success();
    }

    @PostMapping("/api/word/addWord")
    public Result addWord(HttpServletRequest request,@RequestBody WordForm form) {
        String username = (String) request.getAttribute("username");
        int res = wordService.addWord(username,form);
        if(res == 0){
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }








}
