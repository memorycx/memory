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
    private String mean;
    private Integer state;
    private LocalDate updateTime;
    private Integer wordId;
}
