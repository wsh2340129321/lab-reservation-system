package com.lab.service;

import com.lab.entity.User;
import com.lab.repository.ReservationMapper;
import com.lab.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private NotificationService notificationService;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("STUDENT");
        }
        user.setStatus("ACTIVE");
        userMapper.insert(user);
        return user;
    }

    public User login(String studentId, String password) {
        User user = userMapper.findByStudentId(studentId);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User findByStudentId(String studentId) {
        return userMapper.findByStudentId(studentId);
    }

    public User update(User user) {
        userMapper.updateById(user);
        return user;
    }

    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userMapper.selectById(id);
        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userMapper.updateById(user);
            return true;
        }
        return false;
    }

    public User updateProfile(Long id, Map<String, String> profile) {
        User user = userMapper.selectById(id);
        if (user != null) {
            if (profile.containsKey("username")) {
                user.setUsername(profile.get("username"));
            }
            if (profile.containsKey("email")) {
                user.setEmail(profile.get("email"));
            }
            if (profile.containsKey("phone")) {
                user.setPhone(profile.get("phone"));
            }
            userMapper.updateById(user);
            return user;
        }
        return null;
    }

    public User updateStatus(Long id, String status) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setStatus(status);
            userMapper.updateById(user);
        }
        return user;
    }

    public User banUser(Long id, String reason) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setStatus("BANNED");
            user.setBanReason(reason);
            userMapper.updateById(user);
            notificationService.create(id, "ACCOUNT_BANNED", "您的账号已被封禁！封禁理由：" + reason);
        }
        return user;
    }

    public User unbanUser(Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setStatus("ACTIVE");
            user.setBanReason(null);
            userMapper.updateById(user);
            notificationService.create(id, "ACCOUNT_UNBANNED", "您的账号已被解封！");
        }
        return user;
    }

    public boolean isUserBanned(Long userId) {
        User user = userMapper.selectById(userId);
        return user != null && "BANNED".equals(user.getStatus());
    }

    public String getBanReason(Long userId) {
        User user = userMapper.selectById(userId);
        return user != null ? user.getBanReason() : null;
    }

    public ResponseEntity<?> getUserReservations(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("reservations", reservationMapper.findByUserId(userId));
        return ResponseEntity.ok(result);
    }

    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    public void delete(Long id) {
        userMapper.deleteById(id);
    }
}