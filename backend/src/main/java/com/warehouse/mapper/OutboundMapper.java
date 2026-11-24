package com.warehouse.mapper;

import com.warehouse.entity.Outbound;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OutboundMapper {

    @Select("SELECT * FROM outbound WHERE outbound_no = #{outboundNo}")
    Outbound findByNo(String outboundNo);

    @Select("SELECT * FROM outbound WHERE id = #{id}")
    Outbound findById(Long id);

    @Select("SELECT * FROM outbound ORDER BY created_at DESC")
    List<Outbound> findAll();

    @Insert("INSERT INTO outbound (outbound_no, order_id, inventory_id, picked_qty, status, tracking_no, created_by, created_at) VALUES (#{outboundNo}, #{orderId}, #{inventoryId}, #{pickedQty}, 'DRAFT', #{trackingNo}, #{createdBy}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Outbound outbound);

    @Update("UPDATE outbound SET picked_qty = #{pickedQty}, status = 'PICKED' WHERE id = #{id} AND status = 'DRAFT'")
    int pick(Long id, Integer pickedQty);

    @Update("UPDATE outbound SET status = 'CONFIRMED', confirmed_by = #{confirmedBy}, confirmed_at = NOW(), tracking_no = #{trackingNo} WHERE id = #{id} AND status = 'PICKED'")
    int confirm(Long id, Long confirmedBy, String trackingNo);
}