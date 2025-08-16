package com.example.memory.Service;


import com.example.memory.Mapper.BookMapper;
import com.example.memory.pojo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    BookMapper bookMapper;


    public List<Book> getBooks() {
        return bookMapper.getBooks();
    }
}
