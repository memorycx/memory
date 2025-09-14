package com.example.memory.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Senses {

    // 英文 + 中文 变成一个字符串；给前端去做
    private String definitionEn; // 英文释义
    private String definitionCn; // 中文释义
    private List<String> examples; // 该义项对应的多个例句
}
