package com.example.memory.Controller;


import com.example.memory.Service.BookService;
import com.example.memory.pojo.Book;
import com.example.memory.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping("/word/getbooks")
    public Result getbooks(){
        List<Book> books = bookService.getBooks();
        return Result.success(books);
    }

}
