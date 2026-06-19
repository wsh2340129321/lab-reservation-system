package com.lab.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("reservation_times")
public class ReservationTime {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long laboratoryId;
    private String startTime;
    private String endTime;
    private Integer maxBookings;
    private Boolean isAvailable;
}