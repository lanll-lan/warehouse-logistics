package com.warehouse.mapper;

import com.warehouse.entity.Outbound;
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
public interface OutboundMapper {

    @Select("SELECT * FROM outbound WHERE outbound_no = #{outboundNo}")
    Outbound findByNo(String outboundNo);

    @Select("SELECT * FROM outbound WHERE id = #{id}")
    Outbound findById(Long id);

    @Select("""
        SELECT * FROM outbound
        <where>
          <if test="outboundNo != null and outboundNo != ''">AND outbound_no LIKE CONCAT('%', #{outboundNo}, '%')</if>
          <if test="status != null and status != ''">AND status = #{status}</if>
        </where>
        ORDER BY created_at DESC
    """)
    List<Outbound> findAll(@Param("outboundNo") String outboundNo, @Param("status") String status);

    @Insert("INSERT INTO outbound (outbound_no, order_id, inventory_id, picked_qty, status, tracking_no, created_by, created_at) VALUES (#{outboundNo}, #{orderId}, #{inventoryId}, #{pickedQty}, 'DRAFT', #{trackingNo}, #{createdBy}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Outbound outbound);

    @Update("UPDATE outbound SET picked_qty = #{pickedQty}, status = 'PICKED' WHERE id = #{id} AND status = 'DRAFT'")
    int pick(@Param("id") Long id, @Param("pickedQty") Integer pickedQty);

    @Update("UPDATE outbound SET status = 'CONFIRMED', confirmed_by = #{confirmedBy}, confirmed_at = NOW(), tracking_no = #{trackingNo} WHERE id = #{id} AND status = 'PICKED'")
    int confirm(@Param("id") Long id, @Param("confirmedBy") Long confirmedBy, @Param("trackingNo") String trackingNo);
}
