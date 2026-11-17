package com.warehouse.service;

import com.warehouse.entity.Inventory;
import com.warehouse.entity.Outbound;
import com.warehouse.mapper.OutboundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutboundService {

    @Autowired
    private OutboundMapper outboundMapper;

    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public Outbound draft(Outbound outbound) {
        outboundMapper.insert(outbound);
        return outbound;
    }

    @Transactional
    public void pick(Long outboundId, Integer pickedQty) {
        Outbound outbound = outboundMapper.findById(outboundId);
        if (outbound == null || !"DRAFT".equals(outbound.getStatus())) {
            throw new RuntimeException("单据状态异常");
        }
        outboundMapper.pick(outboundId, pickedQty);
    }

    @Transactional
    public void confirm(Long outboundId, String trackingNo, Long confirmedBy) {
        Outbound outbound = outboundMapper.findById(outboundId);
        if (outbound == null || !"PICKED".equals(outbound.getStatus())) {
            throw new RuntimeException("单据状态异常");
        }
        Inventory inv = inventoryService.getById(outbound.getInventoryId());
        if (inv.getQuantity() < outbound.getPickedQty()) {
            throw new RuntimeException("库存不足");
        }
        inventoryService.adjust(outbound.getInventoryId(), -outbound.getPickedQty(), confirmedBy);
        outboundMapper.confirm(outboundId, confirmedBy, trackingNo);
    }

    public List<Outbound> list(String outboundNo, String status) {
        List<Outbound> all = outboundMapper.findAll();
        if (outboundNo != null && !outboundNo.isEmpty() && status != null && !status.isEmpty()) {
            return all.stream()
                    .filter(o -> o.getOutboundNo().contains(outboundNo) && o.getStatus().equals(status))
                    .collect(Collectors.toList());
        } else if (outboundNo != null && !outboundNo.isEmpty()) {
            return all.stream()
                    .filter(o -> o.getOutboundNo().contains(outboundNo))
                    .collect(Collectors.toList());
        } else if (status != null && !status.isEmpty()) {
            return all.stream()
                    .filter(o -> o.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        return all;
    }

    public Outbound getById(Long id) {
        return outboundMapper.findById(id);
    }
}