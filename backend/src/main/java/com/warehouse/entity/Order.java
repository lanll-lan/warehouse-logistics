package com.warehouse.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private String customerName;
    private String productName;
    private Integer quantity;
    private String status;
    private LocalDateTime createTime;
}
