package com.lab.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.ReservationTime;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationTimeMapper extends BaseMapper<ReservationTime> {
    List<ReservationTime> findByLaboratoryId(Long laboratoryId);
}