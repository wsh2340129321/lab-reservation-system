package com.lab.service;

import com.lab.entity.Notification;
import com.lab.entity.Reservation;
import com.lab.entity.ReservationTime;
import com.lab.repository.NotificationMapper;
import com.lab.repository.ReservationMapper;
import com.lab.repository.ReservationTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ReservationTimeMapper reservationTimeMapper;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final int REMINDER_HOURS_BEFORE = 1;

    public Notification create(Long userId, String type, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setContent(content);
        notification.setIsRead(false);
        notificationMapper.insert(notification);
        return notification;
    }

    public List<Notification> findByUserId(Long userId) {
        return notificationMapper.findByUserId(userId);
    }

    public void markAsRead(Long id) {
        Notification notification = notificationMapper.selectById(id);
        if (notification != null) {
            notification.setIsRead(true);
            notificationMapper.updateById(notification);
        }
    }

    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationMapper.findByUserId(userId);
        for (Notification notification : notifications) {
            if (!notification.getIsRead()) {
                notification.setIsRead(true);
                notificationMapper.updateById(notification);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkUpcomingReservations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();

        List<Reservation> pendingReservations = reservationMapper.selectList(null);
        
        for (Reservation reservation : pendingReservations) {
            if ("PENDING".equals(reservation.getStatus()) && 
                reservation.getReservationDate().equals(today)) {
                
                ReservationTime time = reservationTimeMapper.selectById(reservation.getReservationTimeId());
                if (time != null) {
                    LocalTime startTime = LocalTime.parse(time.getStartTime(), TIME_FORMATTER);
                    LocalTime reminderTime = startTime.minusHours(REMINDER_HOURS_BEFORE);
                    
                    if (currentTime.isAfter(reminderTime.minusMinutes(1)) && 
                        currentTime.isBefore(reminderTime.plusMinutes(1))) {
                        create(reservation.getUserId(), "RESERVATION_REMINDER", 
                               "您预约的实验室时段即将开始（" + time.getStartTime() + "）");
                    }
                }
            }
        }
        
        updateCompletedReservations();
    }
    
    private void updateCompletedReservations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();

        List<Reservation> reservations = reservationMapper.selectList(null);
        
        for (Reservation reservation : reservations) {
            if ((reservation.getStatus().equals("PENDING") || 
                 reservation.getStatus().equals("CONFIRMED"))) {
                
                ReservationTime time = reservationTimeMapper.selectById(reservation.getReservationTimeId());
                if (time != null) {
                    LocalTime endTime = LocalTime.parse(time.getEndTime(), TIME_FORMATTER);
                    
                    if (reservation.getReservationDate().isBefore(today) || 
                        (reservation.getReservationDate().equals(today) && currentTime.isAfter(endTime))) {
                        
                        reservation.setStatus("COMPLETED");
                        reservationMapper.updateById(reservation);
                    }
                }
            }
        }
    }
}