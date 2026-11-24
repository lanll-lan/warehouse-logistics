package com.warehouse.mapper;

import com.warehouse.entity.StockMovement;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 库存流水Mapper。
 * 此文件由牵头人写完整，WLS-02/WLS-05/WLS-06/WLS-07 都复用，任何人不要改这里。
 * 各任务在Service里直接调用即可。
 */
@Mapper
public interface StockMovementMapper {

    @Insert("INSERT INTO stock_movement (inventory_id, inbound_line_id, outbound_id, count_id, movement_type, delta_qty, before_qty, after_qty, operator_id) " +
            "VALUES (#{inventoryId}, #{inboundLineId}, #{outboundId}, #{countId}, #{movementType}, #{deltaQty}, #{beforeQty}, #{afterQty}, #{operatorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(StockMovement movement);

    @Select("SELECT * FROM stock_movement ORDER BY occurred_at DESC")
    List<StockMovement> findAll();

    @Select("SELECT * FROM stock_movement WHERE inventory_id = #{inventoryId} ORDER BY occurred_at DESC")
    List<StockMovement> findByInventoryId(Long inventoryId);

    @Select("SELECT * FROM stock_movement WHERE movement_type = #{movementType} ORDER BY occurred_at DESC")
    List<StockMovement> findByType(String movementType);

    // 幂等用：判断某来源是否已写过流水
    @Select("SELECT * FROM stock_movement WHERE outbound_id = #{outboundId}")
    StockMovement findByOutboundId(Long outboundId);

    @Select("SELECT * FROM stock_movement WHERE inbound_line_id = #{inboundLineId}")
    StockMovement findByInboundLineId(Long inboundLineId);

    @Select("SELECT * FROM stock_movement WHERE count_id = #{countId}")
    StockMovement findByCountId(Long countId);
}
