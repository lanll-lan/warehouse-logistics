package com.warehouse.mapper;

import com.warehouse.entity.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LocationMapper {

    @Select("SELECT * FROM location WHERE id = #{id}")
    Location findById(Long id);

    @Select("<script>SELECT * FROM location" +
            "<if test='enabled != null'> WHERE enabled = #{enabled}</if>" +
            " ORDER BY id</script>")
    List<Location> findAll(Boolean enabled);
}
