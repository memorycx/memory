package com.example.memory.mapper;


import com.example.memory.pojo.User;
import com.example.memory.pojo.Word;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WordMapper {



    // 得到用户目前在背的词汇书
    @Select("SELECT current_book_id, new_learn_plane FROM users WHERE username = #{username};")
    User getCurrentBookId(String username);


    List<Word> getBookWord(Integer id);



    List<Word> getNotAlreadyWord(String username,Integer id);

    @Select("SELECT word_id, state, update_time FROM user_word_state WHERE username = #{username} ORDER BY state;")
    List<Word> getAllReviewWord(String username);

    List<Word> getReviewWord(List<Integer> wordsId);

    void learn(List<Integer> wordList, String username, LocalDate now,Integer state);

    void reviewed(List<Integer> wordList, String username, LocalDate now);
}
