package com.warehouse.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Inventory {
    private Long id;
    private String productName;
    private Integer quantity;
    private String warehouseLocation;
    private LocalDateTime updateTime;
}
