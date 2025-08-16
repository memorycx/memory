package com.example.memory;

import com.example.memory.Mapper.BookMapper;
import com.example.memory.Service.UserService;
import com.example.memory.Service.WordService;
import com.example.memory.Tool.JwtUtils;
import com.example.memory.pojo.Book;
import com.example.memory.pojo.Word;
import com.example.memory.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MemoryApplicationTests {

    @Autowired
    BookMapper bookService;
    @Test
    void printBooks() {
        List<Book> books = bookService.getBooks();
        for (Book book : books) {
            System.out.println(book.getBookName());
        }
    }

    @Autowired
    WordService wordService;
    @Test
    void printWords() {
        List<Word> words = wordService.getWord(1);
        for (Word word : words) {
            System.out.println(word.getWord());
        }
    }

    @Test
    void generateToken(){
        String token = JwtUtils.generateToken("xxxx");
        System.out.println(token);
        System.out.println(JwtUtils.verifyToken(token));
    }

//    @Autowired
//    UserService userService;
//    @Test
//    void getUser(){
//        User user = new User("qiushui","123");
//        User tem = userService.getUser(user);
//        System.out.println(tem.getId());
//    }

//    @Test
//    void addUser(){
//        User user = new User("shazi","123","232");
//        System.out.println(userService.register(user));
//    }

}
