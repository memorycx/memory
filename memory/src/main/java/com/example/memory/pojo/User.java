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

    private int newLearnPlane;
    private int reviewPlane;

}
