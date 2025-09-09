package com.example.memory.mapper;


import com.example.memory.pojo.Book;
import com.example.memory.pojo.User;
import com.example.memory.pojo.Word;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WordMapper {

    // 获得一本书的单词数量
    @Select("SELECT vocabulary_num FROM vocabulary_books WHERE id = #{bookId};")
    int searchBookInfo(int bookId);

    // 得到用户目前在背的词汇书
    @Select("SELECT current_book_id  FROM users WHERE username = #{username};")
    int getCurrentBookId(String username);

    // 得到用户所有的词汇书
    @Select("SELECT * FROM vocabulary_books;")
    List<Book> getBookList();

    //获取所有需要复习的单词
    List<Word> getReviewWord(String username);

    // 1.获取背诵书籍的所有单词
    // 2.获取用户已经背诵的该书单词
    // 3.将1中的单词去掉2中出现的单词
    List<Word> getLearnWord(String username,int bookId);
}
