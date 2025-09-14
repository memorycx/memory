package com.example.memory.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word {
    // 这里是所有单词中共用的字段
    private String word;

    // 这里封装all_word 表中的字段
    private String meaning;
    private Integer state;
    private LocalDate updateTime;
    private Integer wordId;

    // 下面这里封装oxford_word 表中的字段
    private String part_of_speech;
    private List<Senses> senses;

    public String toString()
    {
        return "wordId:" + wordId;
    }
}
