package com.lab.controller;

import com.lab.entity.ReservationTime;
import com.lab.service.ReservationTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation-times")
public class ReservationTimeController {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReservationTime reservationTime) {
        ReservationTime createdReservationTime = reservationTimeService.create(reservationTime);
        return ResponseEntity.ok(createdReservationTime);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ReservationTime reservationTime) {
        reservationTime.setId(id);
        ReservationTime updatedReservationTime = reservationTimeService.update(reservationTime);
        return ResponseEntity.ok(updatedReservationTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.ok("删除成功");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ReservationTime reservationTime = reservationTimeService.findById(id);
        return ResponseEntity.ok(reservationTime);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<ReservationTime> reservationTimes = reservationTimeService.findAll();
        return ResponseEntity.ok(reservationTimes);
    }

    @GetMapping("/laboratory/{laboratoryId}")
    public ResponseEntity<?> findByLaboratoryId(@PathVariable Long laboratoryId) {
        List<ReservationTime> reservationTimes = reservationTimeService.findByLaboratoryId(laboratoryId);
        return ResponseEntity.ok(reservationTimes);
    }
}