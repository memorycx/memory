package com.example.memory.Controller;


import com.example.memory.Service.WordService;
import com.example.memory.pojo.Result;
import com.example.memory.pojo.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
