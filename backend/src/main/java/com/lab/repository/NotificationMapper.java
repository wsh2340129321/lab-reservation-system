package com.lab.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
    List<Notification> findByUserId(Long userId);
}