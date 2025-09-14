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
    @Insert("INSERT INTO user_text_word (username, word_id, text_id) VALUES (#{username}, #{wordId}, #{textId});")
    void addUnknownWord(String username, Integer wordId, Integer textId);

    // 统计未知单词是否存在
    @Select("SELECT COUNT(*) FROM user_text_word WHERE username = #{username} AND word_id = #{wordId} AND text_id = #{textId};")
    int countUnknownWord(String username, Integer wordId, Integer textId);

    // 获得陌生单词
    List<Word> getUnknownWord(String username, Integer textId);

    // 删除陌生单词
    @Delete("DELETE FROM user_text_word WHERE username = #{username} AND word_id = #{wordId} AND text_id = #{textId};")
    int deleteUnknownWord(String username, Integer wordId, Integer textId);

    // 标记文章为已读
    @Insert("INSERT INTO user_text_status (username, text_id,status) VALUES (#{username}, #{id},1);")
    void finishRead(String username, Integer id);
}
