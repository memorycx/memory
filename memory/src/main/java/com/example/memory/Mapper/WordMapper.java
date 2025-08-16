package com.example.memory.Mapper;


import com.example.memory.pojo.Word;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WordMapper {
    /**
     * 获取书籍的所有单词id
     * @param id 书籍id
     * @return 单词id列表
     */
    @Insert("INSERT INTO temp_ids(id) select word_id from book_word_mappings where vocabulary_book_id = #{id};")
    void insertTempIds(Integer id);

    /**
     * 获取单词
     * @param id 单词id
     * @return 单词,单词意思
     */
    @Select("SELECT all_word.word,all_word.mean FROM all_word\n" +
            "                    JOIN temp_ids ti ON all_word.id = ti.id;")
    List<Word> getWord(Integer id);

    /*
     * 每次查询单词前，先把临时表清空
     */
    @Delete("DELETE FROM temp_ids;")
    void deleteTemAll();



}
