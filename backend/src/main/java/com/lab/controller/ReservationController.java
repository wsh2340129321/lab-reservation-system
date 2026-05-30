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
            List<Reservation> reservations = reservationService.findByConditions(laboratoryId, studentId, start, end, status);
            return ResponseEntity.ok(reservations);
        }
        List<Reservation> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
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
        List<Reservation> reservations = reservationService.findByConditions(laboratoryId, studentId, start, end, status);
        StringBuilder csv = new StringBuilder();
        csv.append("预约ID,用户ID,实验室ID,时段ID,预约日期,状态,创建时间\n");
        for (Reservation r : reservations) {
            csv.append(r.getId()).append(",")
               .append(r.getUserId()).append(",")
               .append(r.getLaboratoryId()).append(",")
               .append(r.getReservationTimeId()).append(",")
               .append(r.getReservationDate()).append(",")
               .append(r.getStatus()).append(",")
               .append(r.getCreatedAt()).append("\n");
        }
        return ResponseEntity.ok(csv.toString());
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