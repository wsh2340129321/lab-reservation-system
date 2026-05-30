package com.lab.service;

import com.lab.entity.ReservationTime;
import com.lab.repository.ReservationTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationTimeService {
    @Autowired
    private ReservationTimeMapper reservationTimeMapper;

    public ReservationTime create(ReservationTime reservationTime) {
        reservationTimeMapper.insert(reservationTime);
        return reservationTime;
    }

    public ReservationTime update(ReservationTime reservationTime) {
        reservationTimeMapper.updateById(reservationTime);
        return reservationTime;
    }

    public void delete(Long id) {
        reservationTimeMapper.deleteById(id);
    }

    public ReservationTime findById(Long id) {
        return reservationTimeMapper.selectById(id);
    }

    public List<ReservationTime> findByLaboratoryId(Long laboratoryId) {
        return reservationTimeMapper.findByLaboratoryId(laboratoryId);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeMapper.selectList(null);
    }
}