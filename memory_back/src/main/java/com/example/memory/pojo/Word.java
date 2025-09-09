package com.example.memory.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word {
    private String word;
    private String meaning;
    private Integer state;
    private LocalDate updateTime;
    private Integer wordId;

    public String toString()
    {
        return "wordId:" + wordId;
    }
}
