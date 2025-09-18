package com.example.memory.Service;

import com.example.memory.mapper.UserMapper;
import com.example.memory.mapper.WordMapper;
import com.example.memory.pojo.Book;
import com.example.memory.pojo.User;
import com.example.memory.pojo.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class WordService {

    @Autowired
    WordMapper wordMapper;

    @Autowired
    UserMapper userMapper;

    /**
     * 获取书籍的单词数量
     */
    public int searchBookInfo(String username){
        int id = wordMapper.getCurrentBookId(username);
        return wordMapper.searchBookInfo(id);
    }


    public String getCurrentBookId(String username) {
        int bookId = wordMapper.getCurrentBookId(username);
        return wordMapper.getCurrentBookName(bookId);
    }

    public List<Book> getBookList() {
        return wordMapper.getBookList();
    }

    public List<Word> getWordList(String username) {
        LocalDate date = LocalDate.now();
        int bookId = wordMapper.getCurrentBookId(username);

        // num 为今日已经背了的单词数
        Integer num = wordMapper.getNums(username);
        if(num == null) num = 0;

        Integer target = wordMapper.getTargetNums(username);
        int end = target - num;

        // 这里需要走两步逻辑 1.拿到复习单词 2.拿到复习单词
        List<Word> wordList = new ArrayList<>();

        // 1. 拿到复习单词
        List<Word> reviewWordList = wordMapper.getReviewWord(username);
        for(Word word : reviewWordList){
            if(word.getState() == 1 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 1){
                wordList.add(word);
            }else if(word.getState() == 2 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 2){
                wordList.add(word);
            }else if(word.getState() == 3 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 3){
                wordList.add(word);
            }else if(word.getState() == 4 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 4){
                wordList.add(word);
            }else if(word.getState() == 5 && Math.abs(ChronoUnit.DAYS.between(date, word.getUpdateTime())) >= 27){
                wordList.add(word);
            }
        }
        // 2. 拿到背诵单词
        List<Word> learnWordList = wordMapper.getLearnWord(username,bookId);
        if (learnWordList != null && !learnWordList.isEmpty()) {
            if (learnWordList.size() <= end) {
                wordList.addAll(learnWordList);
            } else {
                Random random = new Random();
                for (int i = 0; i < end; i++) {
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

    public Map<String,Integer> getNums(String username) {
        Map<String,Integer> map = new HashMap<>();
        Integer learnNum = wordMapper.getNums(username);
        Integer reviewNum = wordMapper.getReviewNum(username);
        if(reviewNum == null) reviewNum = 0;
        if(learnNum == null) learnNum = 0;
        map.put("reviewNum",reviewNum);
        map.put("learnNum",learnNum);
        return map;
    }

    public List<Word> getContinueLearnWord(String username) {
        int bookId = wordMapper.getCurrentBookId(username);
        List<Word> wordList = new ArrayList<>();
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

    public void setKnow(Integer wordId,Integer state, String username) {
        // 1.检查单词状态
        // 如果是0，则需要添加
        // 如果不是0，则需要更新单词状态
        Word word = new Word();
        word.setWordId(wordId);
        word.setState(state);


        if(state == 0){
            wordMapper.addWordStatus(wordId,username);
            userMapper.updateNewLearnToday(username);
        }else{
            wordMapper.updateWordStatus(wordId,username);
            userMapper.updateReviewToday(username);
        }
        userMapper.updateWordNums(username);
    }


}
