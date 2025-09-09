package com.example.memory.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private int gender;
    private String username;
    private String pwd;
    private String email;
    private String address;
    private String school;

    private int speekNums;
    private int wordNums;
    private int textNums;

    private int newLearnPlane;
    private int reviewPlane;


    private int currentBookId;

    private int continueDay;
    private LocalDate updateTime;

}
