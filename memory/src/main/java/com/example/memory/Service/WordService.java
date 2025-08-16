package com.example.memory.Service;


import com.example.memory.Mapper.WordMapper;
import com.example.memory.pojo.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordService {

    @Autowired
    WordMapper wordMapper;

    public List<Word> getWord(Integer id) {
        wordMapper.deleteTemAll();
        wordMapper.insertTempIds(id);
        List<Word> words = wordMapper.getWord(id);
        wordMapper.deleteTemAll();
        return words;
    }

}
