package com.warehouse.mapper;

import com.warehouse.entity.Inventory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    @Select("""
        SELECT * FROM inventory
        <where>
          <if test="productId != null">AND product_id = #{productId}</if>
          <if test="locationId != null">AND location_id = #{locationId}</if>
        </where>
        ORDER BY id
    """)
    List<Inventory> findAllBy(@Param("productId") Long productId, @Param("locationId") Long locationId);

    @Insert("INSERT INTO inventory (product_id, location_id, quantity, version) VALUES (#{productId}, #{locationId}, #{quantity}, 0)")
    @org.apache.ibatis.annotations.Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Inventory inventory);

    @Update("UPDATE inventory SET quantity = quantity + #{delta}, version = version + 1, updated_at = NOW() WHERE id = #{id} AND version = #{version}")
    int updateQuantity(@Param("id") Long id, @Param("delta") Integer delta, @Param("version") Long version);

    @Update("UPDATE inventory SET quantity = #{quantity}, version = version + 1, updated_at = NOW() WHERE id = #{id} AND version = #{version}")
    int setQuantity(@Param("id") Long id, @Param("quantity") Integer quantity, @Param("version") Long version);
}
