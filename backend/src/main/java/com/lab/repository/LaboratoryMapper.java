package com.lab.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.Laboratory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LaboratoryMapper extends BaseMapper<Laboratory> {
    @Select("SELECT DISTINCT type FROM laboratories WHERE type IS NOT NULL AND type != '' ORDER BY type")
    List<String> findAllTypes();
}