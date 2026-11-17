package com.warehouse.mapper;

import com.warehouse.entity.Inventory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface InventoryMapper {

    @Select("SELECT * FROM inventory WHERE product_id = #{productId} AND location_id = #{locationId}")
    Inventory findByProductAndLocation(Long productId, Long locationId);

    @Select("SELECT * FROM inventory WHERE id = #{id}")
    Inventory findById(Long id);

    @Select("SELECT * FROM inventory ORDER BY id")
    List<Inventory> findAll();

    @Insert("INSERT INTO inventory (product_id, location_id, quantity, version) VALUES (#{productId}, #{locationId}, #{quantity}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Inventory inventory);

    @Update("UPDATE inventory SET quantity = quantity + #{delta}, version = version + 1, updated_at = NOW() WHERE id = #{id}")
    int updateQuantity(Long id, Integer delta);

    @Update("UPDATE inventory SET quantity = #{quantity}, version = version + 1, updated_at = NOW() WHERE id = #{id}")
    int setQuantity(Long id, Integer quantity);
}