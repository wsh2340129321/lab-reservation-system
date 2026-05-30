package com.lab.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("laboratories")
public class Laboratory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String location;
    private String type;
    private Integer capacity;
    private String equipment;
    private String rules;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}