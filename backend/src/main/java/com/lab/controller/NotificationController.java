package com.lab.controller;

import com.lab.entity.Notification;
import com.lab.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.findByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("标记为已读");
    }

    @PostMapping("/user/{userId}/read-all")
    public ResponseEntity<?> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("全部标记为已读");
    }
}