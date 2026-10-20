package com.warehouse.mapper;

import com.warehouse.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderMapper {
    List<Order> findAll();
    Order findById(@Param("id") Long id);
    int insert(Order order);
    int update(Order order);
    int deleteById(@Param("id") Long id);
}
