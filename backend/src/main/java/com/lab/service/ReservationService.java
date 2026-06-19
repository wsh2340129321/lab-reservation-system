package com.lab.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lab.entity.Laboratory;
import com.lab.entity.Reservation;
import com.lab.entity.ReservationTime;
import com.lab.entity.User;
import com.lab.repository.LaboratoryMapper;
import com.lab.repository.ReservationMapper;
import com.lab.repository.ReservationTimeMapper;
import com.lab.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab.dto.ReservationDetailDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ReservationTimeMapper reservationTimeMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LaboratoryMapper laboratoryMapper;

    private static final int MAX_DAILY_HOURS = 8;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Reservation create(Reservation reservation) {
        reservation.setStatus("PENDING");
        reservationMapper.insert(reservation);
        
        Laboratory lab = laboratoryMapper.selectById(reservation.getLaboratoryId());
        ReservationTime time = reservationTimeMapper.selectById(reservation.getReservationTimeId());
        
        String labName = lab != null ? lab.getName() : "未知实验室";
        String timeInfo = time != null ? time.getStartTime().substring(0, 5) + "-" + time.getEndTime().substring(0, 5) : "未知时段";
        String dateInfo = reservation.getReservationDate().toString();
        
        String content = "\u9884\u7EA6\u6210\u529F\uFF01\n\u5B9E\u9A8C\u5BA4\uFF1A" + labName + "\n\u65F6\u6BB5\uFF1A" + dateInfo + " " + timeInfo;
        notificationService.create(reservation.getUserId(), "RESERVATION_SUCCESS", content);
        
        return reservation;
    }

    public Reservation update(Reservation reservation) {
        reservationMapper.updateById(reservation);
        return reservation;
    }

    public void cancel(Long id, Long userId) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation != null) {
            reservation.setStatus("CANCELLED");
            reservationMapper.updateById(reservation);
            notificationService.create(userId, "RESERVATION_CANCEL", "预约已取消！");
        }
    }

    public void cancelByAdmin(Long id, Long userId) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation != null) {
            reservation.setStatus("CANCELLED");
            reservationMapper.updateById(reservation);
            notificationService.create(userId, "RESERVATION_CANCELLED_BY_ADMIN", "您的预约因违规已被管理员取消！");
        }
    }

    public void reject(Long id, String reason) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation != null) {
            reservation.setStatus("REJECTED");
            reservation.setRejectReason(reason);
            reservationMapper.updateById(reservation);
            String content = "您的预约申请已被驳回！\n驳回理由：" + (reason != null ? reason : "无");
            notificationService.create(reservation.getUserId(), "RESERVATION_REJECTED", content);
        }
    }

    public Reservation findById(Long id) {
        return reservationMapper.selectById(id);
    }

    public List<Reservation> findByUserId(Long userId) {
        return reservationMapper.findByUserId(userId);
    }

    public List<Reservation> findByUserIdAndStatus(Long userId, String status) {
        return reservationMapper.findByUserIdAndStatus(userId, status);
    }

    public List<Reservation> findByLaboratoryIdAndDate(Long laboratoryId, LocalDate date) {
        return reservationMapper.findByLaboratoryIdAndDate(laboratoryId, date);
    }

    public List<Reservation> findAll() {
        return reservationMapper.selectList(null);
    }

    public List<Reservation> findByConditions(Long laboratoryId, String studentId, LocalDate startDate, LocalDate endDate, String status) {
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
        
        if (laboratoryId != null) {
            queryWrapper.eq(Reservation::getLaboratoryId, laboratoryId);
        }
        
        if (studentId != null && !studentId.isEmpty()) {
            User user = userMapper.findByStudentId(studentId);
            if (user != null) {
                queryWrapper.eq(Reservation::getUserId, user.getId());
            }
        }
        
        if (startDate != null) {
            queryWrapper.ge(Reservation::getReservationDate, startDate);
        }
        
        if (endDate != null) {
            queryWrapper.le(Reservation::getReservationDate, endDate);
        }
        
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq(Reservation::getStatus, status);
        }
        
        queryWrapper.orderByDesc(Reservation::getReservationDate, Reservation::getCreatedAt);
        
        return reservationMapper.selectList(queryWrapper);
    }

    public boolean checkAvailability(Long laboratoryId, Long reservationTimeId, LocalDate date) {
        List<Reservation> reservations = reservationMapper.findByLaboratoryIdAndDate(laboratoryId, date);
        for (Reservation reservation : reservations) {
            if (reservation.getReservationTimeId().equals(reservationTimeId) && 
                (reservation.getStatus().equals("PENDING") || reservation.getStatus().equals("CONFIRMED"))) {
                return false;
            }
        }
        return true;
    }

    public String validateReservation(Reservation reservation) {
        User user = userMapper.selectById(reservation.getUserId());
        if (user != null && "BANNED".equals(user.getStatus())) {
            return "您的账号已被封禁，无法进行预约！封禁理由：" + (user.getBanReason() != null ? user.getBanReason() : "未说明");
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();
        
        if (reservation.getReservationDate().isBefore(today)) {
            return "不能预约过去的日期";
        }
        
        if (reservation.getReservationDate().equals(today)) {
            ReservationTime time = reservationTimeMapper.selectById(reservation.getReservationTimeId());
            if (time != null) {
                LocalTime startTime = LocalTime.parse(time.getStartTime(), TIME_FORMATTER);
                if (currentTime.isAfter(startTime)) {
                    return "该时段已过，无法预约";
                }
            }
        }
        
        if (!checkAvailability(reservation.getLaboratoryId(), reservation.getReservationTimeId(), reservation.getReservationDate())) {
            return "该时段已被预约";
        }
        
        int dailyMinutes = calculateDailyReservationMinutes(reservation.getUserId(), reservation.getReservationDate());
        int newMinutes = getReservationDuration(reservation.getReservationTimeId());
        
        if ((dailyMinutes + newMinutes) / 60 > MAX_DAILY_HOURS) {
            return "单日预约时长超过上限(" + MAX_DAILY_HOURS + "小时)";
        }
        
        return null;
    }

    private int calculateDailyReservationMinutes(Long userId, LocalDate date) {
        List<Reservation> reservations = reservationMapper.findByUserId(userId);
        int totalMinutes = 0;
        
        for (Reservation reservation : reservations) {
            if (reservation.getReservationDate().equals(date) && 
                ("PENDING".equals(reservation.getStatus()) || "CONFIRMED".equals(reservation.getStatus()))) {
                totalMinutes += getReservationDuration(reservation.getReservationTimeId());
            }
        }
        return totalMinutes;
    }

    private int getReservationDuration(Long timeId) {
        ReservationTime time = reservationTimeMapper.selectById(timeId);
        if (time == null) return 0;
        
        LocalTime start = LocalTime.parse(time.getStartTime(), TIME_FORMATTER);
        LocalTime end = LocalTime.parse(time.getEndTime(), TIME_FORMATTER);
        return java.time.Duration.between(start, end).toMinutesPart() + 
               java.time.Duration.between(start, end).toHoursPart() * 60;
    }

    public List<ReservationDetailDTO> findAllWithDetails() {
        List<Reservation> reservations = reservationMapper.selectList(null);
        return convertToDetailDTO(reservations);
    }

    public List<ReservationDetailDTO> findByConditionsWithDetails(Long laboratoryId, String studentId, LocalDate startDate, LocalDate endDate, String status) {
        List<Reservation> reservations = findByConditions(laboratoryId, studentId, startDate, endDate, status);
        return convertToDetailDTO(reservations);
    }

    private List<ReservationDetailDTO> convertToDetailDTO(List<Reservation> reservations) {
        List<ReservationDetailDTO> detailList = new ArrayList<>();
        
        for (Reservation reservation : reservations) {
            User user = userMapper.selectById(reservation.getUserId());
            Laboratory lab = laboratoryMapper.selectById(reservation.getLaboratoryId());
            ReservationTime time = reservationTimeMapper.selectById(reservation.getReservationTimeId());
            
            String studentId = user != null ? user.getStudentId() : "";
            String userName = user != null ? user.getUsername() : "";
            String labName = lab != null ? lab.getName() : "";
            String startTime = time != null ? time.getStartTime() : "";
            String endTime = time != null ? time.getEndTime() : "";
            
            detailList.add(new ReservationDetailDTO(reservation, studentId, userName, labName, startTime, endTime));
        }
        
        return detailList;
    }
}