package com.warehouse.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogisticsEvent {
    private Long id;
    private String externalEventId;
    private Long outboundId;
    private String status;
    private LocalDateTime eventTime;
    private LocalDateTime receivedAt;
}
