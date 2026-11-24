package com.warehouse.mapper;

import com.warehouse.entity.Location;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * WLS-04 库位Mapper骨架。SQL实现由该任务负责人补充。
 */
@Mapper
public interface LocationMapper {

    // TODO(WLS-04): SELECT * FROM location WHERE id = #{id}
    Location findById(Long id);

    // TODO(WLS-04): SELECT * FROM location ORDER BY id，enabled过滤按需加
    List<Location> findAll(Boolean enabled);
}
