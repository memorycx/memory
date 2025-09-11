package com.example.memory.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class Text {
    private int id;
    private int nums;
    private String title;
    private String summary;
    private String source;
    private String content;
    private LocalDate date;
    private Map<Integer,String> endContent;
}
