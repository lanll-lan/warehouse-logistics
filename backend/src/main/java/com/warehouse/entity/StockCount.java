package com.warehouse.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StockCount {
    private Long id;
    private String countNo;
    private Long inventoryId;
    private Integer systemQty;
    private Integer actualQty;
    private Long stockVersion;
    private String reason;
    private Long countedBy;
    private LocalDateTime countedAt;
}
