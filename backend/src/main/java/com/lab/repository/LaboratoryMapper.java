package com.lab.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.Laboratory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LaboratoryMapper extends BaseMapper<Laboratory> {
}