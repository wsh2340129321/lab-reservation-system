package com.lab.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lab.entity.Laboratory;
import com.lab.entity.Reservation;
import com.lab.entity.ReservationTime;
import com.lab.repository.LaboratoryMapper;
import com.lab.repository.ReservationMapper;
import com.lab.repository.ReservationTimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LaboratoryService {
    @Autowired
    private LaboratoryMapper laboratoryMapper;
    @Autowired
    private ReservationTimeMapper reservationTimeMapper;
    @Autowired
    private ReservationMapper reservationMapper;

    public Laboratory create(Laboratory laboratory) {
        laboratoryMapper.insert(laboratory);
        return laboratory;
    }

    public Laboratory update(Laboratory laboratory) {
        laboratoryMapper.updateById(laboratory);
        return laboratory;
    }

    public void delete(Long id) {
        laboratoryMapper.deleteById(id);
    }

    public Laboratory findById(Long id) {
        return laboratoryMapper.selectById(id);
    }

    public List<Laboratory> findAll() {
        return laboratoryMapper.selectList(null);
    }

    public List<Laboratory> findByCondition(String name, String type, String location, String keyword) {
        QueryWrapper<Laboratory> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("name", keyword).or().like("location", keyword).or().like("type", keyword));
        } else {
            if (name != null && !name.isEmpty()) {
                wrapper.like("name", name);
            }
            if (type != null && !type.isEmpty()) {
                wrapper.eq("type", type);
            }
            if (location != null && !location.isEmpty()) {
                wrapper.like("location", location);
            }
        }
        return laboratoryMapper.selectList(wrapper);
    }

    public List<Map<String, Object>> getAvailableTimes(Long laboratoryId, String dateStr) {
        LocalDate date;
        try {
            if (dateStr.contains("T")) {
                date = LocalDate.parse(dateStr.substring(0, 10));
            } else {
                date = LocalDate.parse(dateStr);
            }
        } catch (Exception e) {
            date = LocalDate.now();
        }
        
        List<ReservationTime> allTimes = reservationTimeMapper.findByLaboratoryId(laboratoryId);
        List<Reservation> reservations = reservationMapper.findByLaboratoryIdAndDate(laboratoryId, date);
        
        List<Long> bookedTimeIds = reservations.stream()
                .filter(r -> "PENDING".equals(r.getStatus()) || "CONFIRMED".equals(r.getStatus()))
                .map(Reservation::getReservationTimeId)
                .collect(Collectors.toList());

        List<Map<String, Object>> availableTimes = new ArrayList<>();
        for (ReservationTime time : allTimes) {
            Map<String, Object> timeInfo = new HashMap<>();
            timeInfo.put("id", time.getId());
            timeInfo.put("startTime", time.getStartTime());
            timeInfo.put("endTime", time.getEndTime());
            timeInfo.put("isAvailable", !bookedTimeIds.contains(time.getId()));
            availableTimes.add(timeInfo);
        }
        return availableTimes;
    }

    public List<String> findAllTypes() {
        return laboratoryMapper.findAllTypes();
    }
}