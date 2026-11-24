package com.warehouse.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Outbound {
    private Long id;
    private String outboundNo;
    private Long orderId;
    private Long inventoryId;
    private Integer pickedQty;
    private String status;
    private String trackingNo;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long confirmedBy;
    private LocalDateTime confirmedAt;
}
