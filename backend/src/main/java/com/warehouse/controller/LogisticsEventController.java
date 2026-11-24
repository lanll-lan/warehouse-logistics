package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.LogisticsEvent;
import com.warehouse.entity.Outbound;
import com.warehouse.service.LogisticsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logistics")
public class LogisticsEventController {

    @Autowired
    private LogisticsEventService logisticsEventService;

    @GetMapping("/pending")
    public Result<List<Outbound>> pending() {
        return Result.success(logisticsEventService.listPending());
    }

    @GetMapping("/{outboundId}")
    public Result<List<LogisticsEvent>> list(@PathVariable Long outboundId) {
        return Result.success(logisticsEventService.listByOutbound(outboundId));
    }

    @PostMapping("/push")
    public Result<LogisticsEvent> push(@RequestBody Map<String, String> params) {
        LogisticsEvent event = logisticsEventService.push(
                params.get("externalEventId"),
                parseLong(params.get("outboundId")),
                params.get("status"),
                parseTime(params.get("eventTime")));
        return Result.success(event);
    }

    private static Long parseLong(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        return Long.valueOf(text.trim());
    }

    private static LocalDateTime parseTime(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String normalized = text.trim().replace(' ', 'T');
        if (normalized.length() == 16) {
            normalized = normalized + ":00";
        }
        return LocalDateTime.parse(normalized);
    }
}
