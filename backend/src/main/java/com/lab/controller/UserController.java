package com.lab.controller;

import com.lab.entity.User;
import com.lab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.update(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        boolean success = userService.updatePassword(id, oldPassword, newPassword);
        if (success) {
            return ResponseEntity.ok("密码修改成功");
        }
        return ResponseEntity.badRequest().body("原密码不正确");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            Map<String, Object> profile = new HashMap<>();
            profile.put("id", user.getId());
            profile.put("studentId", user.getStudentId());
            profile.put("username", user.getUsername());
            profile.put("email", user.getEmail());
            profile.put("phone", user.getPhone());
            profile.put("status", user.getStatus());
            return ResponseEntity.ok(profile);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Map<String, String> profile) {
        User updatedUser = userService.updateProfile(id, profile);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.badRequest().body("更新失败");
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("删除成功");
    }

    @GetMapping("/student-id/{studentId}")
    public ResponseEntity<?> findByStudentId(@PathVariable String studentId) {
        User user = userService.findByStudentId(studentId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        User user = userService.updateStatus(id, status);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<?> getUserReservations(@PathVariable Long id) {
        return userService.getUserReservations(id);
    }
}