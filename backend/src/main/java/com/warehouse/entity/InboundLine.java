package com.warehouse.entity;

import lombok.Data;

@Data
public class InboundLine {
    private Long id;
    private Long inboundId;
    private Long productId;
    private Long locationId;
    private Integer quantity;
}
