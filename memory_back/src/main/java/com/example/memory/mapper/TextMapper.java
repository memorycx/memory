package com.example.memory.mapper;

import com.example.memory.pojo.Text;
import com.example.memory.pojo.Word;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TextMapper {

    @Select("select * from text")
    List<Text> getArticleList();


    // 新增未知单词
    @Insert("INSERT INTO user_text_word (username, word, text_id) VALUES (#{username}, #{word}, #{textId});")
    void addUnknownWord(String username, String word, Integer textId);

    // 统计未知单词是否存在
    @Select("SELECT COUNT(*) FROM user_text_word WHERE username = #{username} AND word = #{word} AND text_id = #{textId};")
    int countUnknownWord(String username, String word, Integer textId);

    // 获得陌生单词
    @Select("select word from user_text_word where username = #{username} and text_id = #{textId}")
    List<Word> getUnknownWord(String username, Integer textId);

    // 删除陌生单词
    @Delete("DELETE FROM user_text_word WHERE username = #{username} AND word = #{word} AND text_id = #{textId};")
    int deleteUnknownWord(String username, String word, Integer textId);

    // 标记文章为已读
    @Insert("INSERT INTO user_text_status (username, text_id,status) VALUES (#{username}, #{id},1);")
    void finishRead(String username, Integer id);

    // 获得已读文章
    @Select("select text_id from user_text_status where username = #{username} and status = 1")
    List<Integer> getFinishReading(String username);
}
