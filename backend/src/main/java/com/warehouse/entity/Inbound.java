package com.warehouse.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Inbound {
    private Long id;
    private String inboundNo;
    private String sourceName;
    private String status;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long confirmedBy;
    private LocalDateTime confirmedAt;
}
