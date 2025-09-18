package com.example.memory.mapper;


import com.example.memory.pojo.Book;
import com.example.memory.pojo.User;
import com.example.memory.pojo.Word;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WordMapper {

    // 获得一本书的单词数量
    @Select("SELECT vocabulary_num FROM vocabulary_books WHERE id = #{bookId};")
    int searchBookInfo(int bookId);

    // 得到用户目前在背的词汇书的id
    @Select("SELECT current_book_id  FROM users WHERE username = #{username};")
    int getCurrentBookId(String username);

    // 得到用户目前在背的书籍
    @Select("SELECT book_name from vocabulary_books where id = #{bookId}")
    String getCurrentBookName(int bookId);

    // 得到用户所有的词汇书
    @Select("SELECT * FROM vocabulary_books;")
    List<Book> getBookList();

    //获取所有需要复习的单词
    List<Word> getReviewWord(String username);

    // 1.获取背诵书籍的所有单词
    // 2.获取用户已经背诵的该书单词
    // 3.将1中的单词去掉2中出现的单词
    List<Word> getLearnWord(String username,int bookId);




    // 获得用户今日需要背诵的单词数
    @Select("SELECT new_learn_plane from users where username = #{username} ")
    Integer getTargetNums(String username);

    // 获得用户今日背诵的单词数
    @Select("SELECT new_learn from user_day_num where username = #{username} and date = CURDATE()")
    Integer getNums(String username);

    // 获得用户今日复习的单词数
    @Select("SELECT review from user_day_num where username = #{username} and date = CURDATE()")
    Integer getReviewNum(String username);

    // 新增单词状态
    @Insert("INSERT INTO user_word_state (username, word_id, state,update_time) VALUES (#{username}, #{wordId}, 6,now());")
    void addWordStatus(Integer wordId, String username);

    // 更新单词状态
    @Update("UPDATE user_word_state SET state = 6,update_time = now() WHERE username = #{username} and word_id = #{wordId};")
    void updateWordStatus(Integer wordId, String username);




}
