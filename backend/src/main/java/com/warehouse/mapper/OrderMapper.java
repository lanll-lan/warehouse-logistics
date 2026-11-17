package com.warehouse.mapper;

import com.warehouse.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Order findByNo(String orderNo);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order findById(Long id);

    @Select("""
        SELECT * FROM orders
        <where>
          <if test="orderNo != null and orderNo != ''">AND order_no LIKE CONCAT('%', #{orderNo}, '%')</if>
          <if test="status != null and status != ''">AND status = #{status}</if>
        </where>
        ORDER BY created_at DESC
    """)
    List<Order> findAll(@Param("orderNo") String orderNo, @Param("status") String status);

    @Insert("INSERT INTO orders (order_no, customer_name, product_id, quantity, status, created_by, created_at) VALUES (#{orderNo}, #{customerName}, #{productId}, #{quantity}, 'PENDING', #{createdBy}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Update("UPDATE orders SET status = 'SHIPPED' WHERE id = #{id} AND status = 'PENDING'")
    int ship(Long id);
}
