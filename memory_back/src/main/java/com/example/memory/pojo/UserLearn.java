package com.example.memory.pojo;

import lombok.Data;

import java.time.LocalDate;

/*
    用于对用户每日学习数据进行一个统计
 */
@Data
public class UserLearn {
    private String username;
    private LocalDate date;
    private String content;
    private int newLearn;
    private int review;
    private int read;
    private int speek;

}
