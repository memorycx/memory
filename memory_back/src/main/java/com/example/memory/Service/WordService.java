package com.example.memory.Service;


import com.example.memory.mapper.WordMapper;
import com.example.memory.pojo.Book;
import com.example.memory.pojo.User;
import com.example.memory.pojo.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class WordService {

    @Autowired
    WordMapper wordMapper;

    /**
     * 获取书籍的单词数量
     */
    public int searchBookInfo(String username){
        int id = wordMapper.getCurrentBookId(username);
        return wordMapper.searchBookInfo(id);
    }


    public int getCurrentBookId(String username) {
        return wordMapper.getCurrentBookId(username);
    }

    public List<Book> getBookList() {
        return wordMapper.getBookList();
    }

    public List<Word> getWordList(String username) {
        LocalDate date = LocalDate.now();
        int bookId = wordMapper.getCurrentBookId(username);
        // 这里需要走两步逻辑 1.拿到复习单词 2.拿到复习单词
        List<Word> wordList = new ArrayList<>();

        // 1. 拿到复习单词
        List<Word> reviewWordList = wordMapper.getReviewWord(username);
        for(Word word : reviewWordList){
            if(word.getState() == 0 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 1){
                wordList.add(word);
            }else if(word.getState() == 1 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 2){
                wordList.add(word);
            }else if(word.getState() == 2 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 3){
                wordList.add(word);
            }else if(word.getState() == 3 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 4){
                wordList.add(word);
            }else if(word.getState() == 4 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 27){
                wordList.add(word);
            }
        }
        // 2. 拿到背诵单词
        List<Word> learnWordList = wordMapper.getLearnWord(username,bookId);
        if (learnWordList != null && !learnWordList.isEmpty()) {
            if (learnWordList.size() <= 20) {
                wordList.addAll(learnWordList);
            } else {
                Random random = new Random();
                for (int i = 0; i < 20; i++) {
                    // 生成不重复的随机索引
                    int randomIndex = random.nextInt(learnWordList.size() - i);
                    wordList.add(learnWordList.get(randomIndex));
                    // 交换已选中元素到尾部，避免重复选取
                    Collections.swap(learnWordList, randomIndex, learnWordList.size() - 1 - i);
                }
            }
        }
        return wordList;
    }
}
