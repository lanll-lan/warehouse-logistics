package com.warehouse.mapper;

import com.warehouse.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product findById(Long id);

    @Select("<script>SELECT * FROM product" +
            "<if test='enabled != null'> WHERE enabled = #{enabled}</if>" +
            " ORDER BY id</script>")
    List<Product> findAll(Boolean enabled);
}
