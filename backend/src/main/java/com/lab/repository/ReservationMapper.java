package com.lab.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByLaboratoryIdAndDate(Long laboratoryId, LocalDate date);
    List<Reservation> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
    void deleteByLaboratoryId(Long laboratoryId);
    void deleteByReservationTimeId(Long reservationTimeId);
}