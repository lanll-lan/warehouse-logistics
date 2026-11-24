package com.warehouse.service;

import com.warehouse.entity.LogisticsEvent;
import com.warehouse.entity.Outbound;
import com.warehouse.mapper.LogisticsEventMapper;
import com.warehouse.mapper.OutboundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogisticsEventService {

    private static final List<String> STATUS_ORDER = Arrays.asList("PICKED_UP", "IN_TRANSIT", "DELIVERED");

    @Autowired
    private LogisticsEventMapper logisticsEventMapper;

    @Autowired
    private OutboundMapper outboundMapper;

    public List<LogisticsEvent> listByOutbound(Long outboundId) {
        if (outboundMapper.findById(outboundId) == null) {
            throw new RuntimeException("出库单不存在");
        }
        return logisticsEventMapper.findByOutboundId(outboundId);
    }

    public List<Outbound> listPending() {
        return outboundMapper.findAll().stream()
                .filter(o -> "CONFIRMED".equals(o.getStatus()))
                .filter(o -> logisticsEventMapper.findByOutboundId(o.getId()).isEmpty())
                .sorted((a, b) -> {
                    if (a.getConfirmedAt() == null) return 1;
                    if (b.getConfirmedAt() == null) return -1;
                    return b.getConfirmedAt().compareTo(a.getConfirmedAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public LogisticsEvent push(String externalEventId, Long outboundId, String status, LocalDateTime eventTime) {
        if (externalEventId == null || externalEventId.trim().isEmpty()) {
            throw new RuntimeException("外部事件号不能为空");
        }
        if (outboundId == null) {
            throw new RuntimeException("出库单号不能为空");
        }
        if (status == null || !STATUS_ORDER.contains(status)) {
            throw new RuntimeException("状态取值非法");
        }
        if (eventTime == null) {
            throw new RuntimeException("事件时间不能为空");
        }

        Outbound outbound = outboundMapper.findById(outboundId);
        if (outbound == null) {
            throw new RuntimeException("出库单不存在");
        }
        if (!"CONFIRMED".equals(outbound.getStatus())) {
            throw new RuntimeException("出库单未确认，不能推送物流事件");
        }

        LogisticsEvent existed = logisticsEventMapper.findByExternalEventId(externalEventId);
        if (existed != null) {
            boolean same = existed.getOutboundId().equals(outboundId)
                    && existed.getStatus().equals(status)
                    && existed.getEventTime().equals(eventTime);
            if (same) {
                return existed;
            }
            throw new RuntimeException("事件号重复但内容不一致");
        }

        List<LogisticsEvent> history = logisticsEventMapper.findByOutboundId(outboundId);
        if (history.isEmpty()) {
            if (!"PICKED_UP".equals(status)) {
                throw new RuntimeException("首条事件必须是已揽收");
            }
        } else {
            LogisticsEvent last = history.get(0);
            if (last.getStatus().equals(status)) {
                throw new RuntimeException("该运单已存在相同状态的事件");
            }
            if (STATUS_ORDER.indexOf(status) != STATUS_ORDER.indexOf(last.getStatus()) + 1) {
                throw new RuntimeException("状态推进不合法");
            }
            if (eventTime.isBefore(last.getEventTime())) {
                throw new RuntimeException("事件时间早于上一条事件");
            }
        }

        LogisticsEvent event = new LogisticsEvent();
        event.setExternalEventId(externalEventId.trim());
        event.setOutboundId(outboundId);
        event.setStatus(status);
        event.setEventTime(eventTime);
        logisticsEventMapper.insert(event);
        return logisticsEventMapper.findById(event.getId());
    }
}
