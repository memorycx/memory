package com.example.memory.controller;


import com.example.memory.Service.UserService;
import com.example.memory.Service.WordService;
import com.example.memory.pojo.Book;
import com.example.memory.pojo.Result;
import com.example.memory.pojo.Word;
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
        int currentBook = wordService.getCurrentBookId(username);
        return Result.success(currentBook);
    }

    /**
     * 获取用户所有的词汇书
     */
    @GetMapping("/api/word/getBookList")
    public Result getBookList() {
        List<Book> bookList = wordService.getBookList();
        return Result.success(bookList);
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
        List<Word> wordList = wordService.getWordList(username);
        return Result.success(wordList);
    }

}
