package com.warehouse.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Inventory {
    private Long id;
    private Long productId;
    private Long locationId;
    private Integer quantity;
    private Long version;
    private LocalDateTime updatedAt;
}
