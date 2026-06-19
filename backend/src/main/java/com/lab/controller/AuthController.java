package com.lab.controller;

import com.lab.entity.User;
import com.lab.security.JwtUtil;
import com.lab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // 检查学号是否已存在
        if (userService.findByStudentId(user.getStudentId()) != null) {
            return ResponseEntity.badRequest().body("学号已存在");
        }
        User registeredUser = userService.register(user);
        String token = jwtUtil.generateToken(registeredUser.getStudentId());
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", registeredUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String studentId = credentials.get("studentId");
        String password = credentials.get("password");
        User user = userService.login(studentId, password);
        if (user == null) {
            return ResponseEntity.badRequest().body("学号或密码错误");
        }
        String token = jwtUtil.generateToken(user.getStudentId());
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return ResponseEntity.ok(response);
    }
}