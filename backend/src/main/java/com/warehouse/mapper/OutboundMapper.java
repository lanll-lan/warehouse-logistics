package com.warehouse.mapper;

import com.warehouse.entity.Outbound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OutboundMapper {
    List<Outbound> findAll();
    Outbound findById(@Param("id") Long id);
    int insert(Outbound outbound);
    int update(Outbound outbound);
    int deleteById(@Param("id") Long id);
}
