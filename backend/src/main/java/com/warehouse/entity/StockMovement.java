package com.warehouse.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StockMovement {
    private Long id;
    private Long inventoryId;
    private Long inboundLineId;
    private Long outboundId;
    private Long countId;
    private String movementType;
    private Integer deltaQty;
    private Integer beforeQty;
    private Integer afterQty;
    private Long operatorId;
    private LocalDateTime occurredAt;
}
