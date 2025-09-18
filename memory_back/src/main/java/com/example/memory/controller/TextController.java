package com.example.memory.controller;

import com.example.memory.Service.TextService;
import com.example.memory.Service.WordService;
import com.example.memory.pojo.Result;
import com.example.memory.pojo.Text;
import com.example.memory.pojo.Word;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class TextController {

    @Autowired
    TextService textService;

    @Autowired
    WordService wordService;

    /*
    需要返回文章的详细信息，每一个属性都要有值
     */
    @GetMapping("/api/article/getArticleList")
    public Result getArticleList(){
        List<Text> articleList = textService.getArticleList();
        return Result.success(articleList);
    }

    /*
      单词查询
     */
//    @GetMapping("/api/word/queryWord")
//    public Result queryWord(@RequestParam String word){
//        List<Word> word1 = wordService.queryWord(word);
//        if(word1 == null || word1.isEmpty()) return Result.error("没查到该单词");
//        return Result.success(word1);
//    }
    /**
     * 新增陌生单词
     */
    @GetMapping("/api/word/addUnknownWord")
    public Result addUnknownWord(HttpServletRequest request, @RequestParam String word,@RequestParam Integer textId) {
        String username = request.getAttribute("username").toString();
        int res = textService.addUnknownWord(username,word,textId);
        if(res == 0) return Result.error("该单词已存在");
        return Result.success("添加成功");
    }
    /*
    删除陌生的单词
     */
    @GetMapping("/api/word/deleteUnknownWord")
    public Result deleteUnknownWord(HttpServletRequest request, @RequestParam String word,@RequestParam Integer textId) {
        String username = request.getAttribute("username").toString();
        int res = textService.deleteUnknownWord(username,word,textId);
        if(res == 0) return Result.error("删除失败");
        return Result.success("删除成功");
    }
    /*
    获得陌生单词
     */
    @GetMapping("/api/word/getUnknownWords")
    public Result getUnknownWord(HttpServletRequest request, @RequestParam Integer textId) {
        String username = request.getAttribute("username").toString();
        List<Word> unknownWord = textService.getUnknownWord(username,textId);
        return Result.success(unknownWord);
    }

    /*
    完成阅读
    1. 添加阅读记录
    2. 更新用户信息（阅读数）
     */
    @GetMapping("/api/article/finishReading")
    public Result finishRead(HttpServletRequest request, @RequestParam Integer id) {
        String username = request.getAttribute("username").toString();
        textService.finishRead(username,id);
        return Result.success();
    }

    @GetMapping("/api/article/getFinishReading")
    public Result getFinishReading(HttpServletRequest request) {
        String username = request.getAttribute("username").toString();
        List<Integer> finishReading = textService.getFinishReading(username);
        return Result.success(finishReading);
    }

}
