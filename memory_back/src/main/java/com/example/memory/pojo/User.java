package com.example.memory.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String username;
    private String pwd;
    private String email;
    private String address;
    private String school;

    private int newLearnPlane;
    private int reviewPlane;

    private int dayLearn;
    private int dayReview;
    private int dayRead;

    private int currentBookId;

}
