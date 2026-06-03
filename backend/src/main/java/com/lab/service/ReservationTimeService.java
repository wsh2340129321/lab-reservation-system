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
    
    @Autowired
    private com.lab.repository.ReservationMapper reservationMapper;

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

    public void deleteByLaboratoryId(Long laboratoryId) {
        List<ReservationTime> times = reservationTimeMapper.findByLaboratoryId(laboratoryId);
        for (ReservationTime time : times) {
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.lab.entity.Reservation> pendingQuery = 
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            pendingQuery.eq(com.lab.entity.Reservation::getReservationTimeId, time.getId())
                       .in(com.lab.entity.Reservation::getStatus, "PENDING", "CONFIRMED");
            List<com.lab.entity.Reservation> pendingReservations = reservationMapper.selectList(pendingQuery);
            if (!pendingReservations.isEmpty()) {
                throw new RuntimeException("该实验室存在待使用的预约，请先取消这些预约");
            }
            reservationMapper.deleteByReservationTimeId(time.getId());
        }
        reservationTimeMapper.deleteByLaboratoryId(laboratoryId);
    }
}