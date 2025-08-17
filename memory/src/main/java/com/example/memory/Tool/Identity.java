package com.example.memory.Tool;

import com.example.memory.pojo.User;
import jakarta.servlet.http.HttpServletRequest;

public class Identity {
    public static void getUsername(User user, HttpServletRequest request) {
        String username = request.getAttribute("username").toString();
        user.setUsername(username);
    }
}
