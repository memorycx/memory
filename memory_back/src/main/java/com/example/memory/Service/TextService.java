package com.example.memory.Service;


import com.example.memory.mapper.TextMapper;
import com.example.memory.mapper.UserMapper;
import com.example.memory.mapper.WordMapper;
import com.example.memory.pojo.Text;
import com.example.memory.pojo.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TextService {

    @Autowired
    TextMapper textMapper;

    @Autowired
    UserMapper userMapper;
    /*
    这里需要做两步
    1. 手动获取文章的summary
    2. 分解content为段落
     */
    public List<Text> getArticleList() {
        List<Text> articleList = textMapper.getArticleList();
        for (Text text : articleList) {
            text.setSummary(text.getContent().substring(0,60)+"...");
            String content = text.getContent();
            String[] paragraphs = content.split("\\n\\n");
            HashMap<Integer,String> endContent = new HashMap<>();
            for (int i = 0; i < paragraphs.length; i++) {
                endContent.put(i,paragraphs[i]);
            }
            text.setEndContent(endContent);
        }
        return articleList;
    }

    /*
    首先判断该单词是否在在本文章中添加过了
    1. 先查询user_text_word表中是否有该单词
    2. 如果有，直接返回并提示已存在
    3. 如果没有，新增
     */
    public int addUnknownWord(String username, String word, Integer textId) {
        int count = textMapper.countUnknownWord(username,word,textId);
        if(count > 0) {
            return 0;
        }
        textMapper.addUnknownWord(username,word,textId);
        return 1;
    }

    public int deleteUnknownWord(String username, String word, Integer textId) {
        return textMapper.deleteUnknownWord(username,word,textId);
    }

    /*
    获得陌生单词
     */
    public List<Word> getUnknownWord(String username, Integer textId) {
        return textMapper.getUnknownWord(username,textId);
    }

    /*
    完成阅读
    1. 添加阅读记录
    2. 更新用户信息（阅读数）
     */
    public void finishRead(String username, Integer id) {
        textMapper.finishRead(username,id);
        userMapper.updateReadNum(username);
        userMapper.updateReadToday(username);
    }

    public List<Integer> getFinishReading(String username) {
        return textMapper.getFinishReading(username);
    }
}
