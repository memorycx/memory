package com.example.memory.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// 映射用户添加单词的表单
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordForm {
    private String content;
    private String bookName;
    boolean newBook;

}
