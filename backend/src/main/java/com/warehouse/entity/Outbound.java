package com.warehouse.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Outbound {
    private Long id;
    private String outboundNo;
    private String productName;
    private Integer quantity;
    private String warehouseLocation;
    private LocalDateTime outboundTime;
}
