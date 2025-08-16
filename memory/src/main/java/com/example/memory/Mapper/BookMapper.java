package com.example.memory.Mapper;

import com.example.memory.pojo.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface BookMapper {
    /**
     * 获取所有书籍
     * @return 书籍列表
     */
    @Select("select * from vocabulary_books")
    List<Book> getBooks();
}
