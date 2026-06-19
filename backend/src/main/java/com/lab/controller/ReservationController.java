package com.lab.controller;

import com.lab.entity.Reservation;
import com.lab.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Reservation reservation) {
        String validationResult = reservationService.validateReservation(reservation);
        if (validationResult != null) {
            return ResponseEntity.badRequest().body(validationResult);
        }
        Reservation createdReservation = reservationService.create(reservation);
        return ResponseEntity.ok(createdReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Reservation reservation) {
        reservation.setId(id);
        Reservation updatedReservation = reservationService.update(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id, @RequestParam Long userId) {
        reservationService.cancel(id, userId);
        return ResponseEntity.ok("取消成功");
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id, @RequestParam(required = false) String reason) {
        reservationService.reject(id, reason);
        return ResponseEntity.ok("驳回成功");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable Long userId, @RequestParam(required = false) String status) {
        List<Reservation> reservations;
        if (status != null && !status.isEmpty()) {
            reservations = reservationService.findByUserIdAndStatus(userId, status);
        } else {
            reservations = reservationService.findByUserId(userId);
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<?> getReservationSummary(@PathVariable Long userId) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("pending", reservationService.findByUserIdAndStatus(userId, "PENDING").size());
        summary.put("completed", reservationService.findByUserIdAndStatus(userId, "COMPLETED").size());
        summary.put("cancelled", reservationService.findByUserIdAndStatus(userId, "CANCELLED").size());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/laboratory/{laboratoryId}/date/{date}")
    public ResponseEntity<?> findByLaboratoryIdAndDate(@PathVariable Long laboratoryId, @PathVariable String date) {
        LocalDate reservationDate = LocalDate.parse(date);
        List<Reservation> reservations = reservationService.findByLaboratoryIdAndDate(laboratoryId, reservationDate);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) Long laboratoryId,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status) {
        if (laboratoryId != null || studentId != null || startDate != null || endDate != null || status != null) {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
            return ResponseEntity.ok(reservationService.findByConditionsWithDetails(laboratoryId, studentId, start, end, status));
        }
        return ResponseEntity.ok(reservationService.findAllWithDetails());
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportReservations(
            @RequestParam(required = false) Long laboratoryId,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        List<com.lab.dto.ReservationDetailDTO> reservations = reservationService.findByConditionsWithDetails(laboratoryId, studentId, start, end, status);
        
        StringBuilder csv = new StringBuilder();
        csv.append("\uFEFF");
        csv.append("预约ID,实验室名称,用户姓名,学号,预约日期,开始时间,结束时间,状态\n");
        for (com.lab.dto.ReservationDetailDTO r : reservations) {
            csv.append(r.getId()).append(",")
               .append(escapeCsv(r.getLaboratoryName())).append(",")
               .append(escapeCsv(r.getUserName())).append(",")
               .append(r.getStudentId()).append(",")
               .append(r.getReservationDate()).append(",")
               .append(r.getStartTime()).append(",")
               .append(r.getEndTime()).append(",")
               .append(getStatusText(r.getStatus())).append("\n");
        }
        
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv; charset=UTF-8")
                .header("Content-Disposition", "attachment; filename=reservations.csv")
                .body(csv.toString());
    }
    
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private String getStatusText(String status) {
        if (status == null) return "";
        java.util.Map<String, String> map = new java.util.HashMap<>();
        map.put("PENDING", "待使用");
        map.put("CONFIRMED", "已确认");
        map.put("COMPLETED", "已使用");
        map.put("CANCELLED", "已取消");
        map.put("REJECTED", "已驳回");
        return map.getOrDefault(status, status);
    }

    @GetMapping("/check-availability")
    public ResponseEntity<?> checkAvailability(
            @RequestParam Long laboratoryId,
            @RequestParam Long reservationTimeId,
            @RequestParam String date) {
        LocalDate reservationDate = LocalDate.parse(date);
        boolean isAvailable = reservationService.checkAvailability(laboratoryId, reservationTimeId, reservationDate);
        return ResponseEntity.ok(isAvailable);
    }
}