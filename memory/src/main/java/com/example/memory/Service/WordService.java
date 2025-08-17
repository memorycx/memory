package com.example.memory.Service;


import com.example.memory.mapper.WordMapper;
import com.example.memory.pojo.User;
import com.example.memory.pojo.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WordService {

    @Autowired
    WordMapper wordMapper;

    /**
     * 获得一本书的所有词汇
     */
    public List<Word> getWord(Integer id) {
        return wordMapper.getBookWord(id);
    }


    /**
     * 2.用户当前学习书籍
     *   2.1 得到该书籍的所有单词
     *   2.2 获得本书背过的单词
     *   2.3 筛选出没有背诵的单词
     */
    public List<Word> getTodayWord(String username) {

        List<Word> words = new ArrayList<>();
        User user  = wordMapper.getCurrentBookId(username);
        int bookId = user.getCurrentBookId();
        int newLearnPlane = user.getNewLearnPlane();
        List<Word> wordsAll = wordMapper.getNotAlreadyWord(username,bookId);

        if(wordsAll.size()  <= newLearnPlane){
            return wordsAll;
        }
        int min = 0;
        int max = wordsAll.size() - 1;

        for(int i = 0;i < newLearnPlane;i++){
            Random random = new Random();
            int rangeInt = min + random.nextInt(max - min + 1); // min ~ max之间的整数
            Word word = wordsAll.get(rangeInt);
            words.add(word);
        }
        return words;
    }

    /**
     * 1. 先获得所有该用户所有待复习的单词
     * 2. 优先复习state等级高的词汇
     * 3. 是否复习校验
     *     -->  当下时间 - 上次更新时间  = 差距天数
     *     -->  差距天数 && state 判断当前是否需要复习
     */
    public List<Word> getReviewWord(String username) {
        List<Word> wordsAll = wordMapper.getAllReviewWord(username);
        List<Integer> wordsId = new ArrayList<>();
        for(Word word : wordsAll){
            // 得到间隔的天数
            int day = word.getUpdateTime().until(LocalDate.now()).getDays();
            if(word.getState() == 4 && day >= 27){
                wordsId.add(word.getWordId());
            }
            else if(word.getState() == 3 && day >= 4){
                wordsId.add(word.getWordId());
            }
            else if(word.getState() == 2 && day >= 2){
                wordsId.add(word.getWordId());
            }
            else if(word.getState() == 1 && day >= 1){
                wordsId.add(word.getWordId());
            }
        }
        return wordMapper.getReviewWord(wordsId);
    }

    public void learn(List<Integer> wordList, String username) {
        LocalDate now = LocalDate.now();
        wordMapper.learn(wordList,username,now,1);
    }

    public void reviewed(List<Integer> wordList, String username) {
        LocalDate now = LocalDate.now();
        wordMapper.reviewed(wordList,username,now);
    }
}
