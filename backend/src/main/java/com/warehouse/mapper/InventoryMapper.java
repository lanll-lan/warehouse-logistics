package com.warehouse.mapper;

import com.warehouse.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface InventoryMapper {
    List<Inventory> findAll();
    Inventory findById(@Param("id") Long id);
    int insert(Inventory inventory);
    int update(Inventory inventory);
    int deleteById(@Param("id") Long id);
}
