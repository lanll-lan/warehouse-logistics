package com.warehouse.controller;

import com.warehouse.entity.Outbound;
import com.warehouse.service.OutboundService;
import com.warehouse.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/outbound")
public class OutboundController {

    @Autowired
    private OutboundService outboundService;

    @PostMapping("/draft")
    public Result<Outbound> draft(@RequestBody Map<String, String> params) {
        Outbound outbound = new Outbound();
        outbound.setOutboundNo(params.get("outboundNo"));
        outbound.setOrderId(Long.valueOf(params.get("orderId")));
        outbound.setInventoryId(Long.valueOf(params.get("inventoryId")));
        outbound.setCreatedBy(Long.valueOf(params.get("createdBy")));
        return Result.success(outboundService.draft(outbound));
    }

    @PutMapping("/{id}/pick")
    public Result<Void> pick(@PathVariable Long id, @RequestBody Map<String, String> params) {
        Integer pickedQty = Integer.valueOf(params.get("pickedQty"));
        outboundService.pick(id, pickedQty);
        return Result.success();
    }

    @PostMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String trackingNo = params.get("trackingNo");
        Long confirmedBy = Long.valueOf(params.get("confirmedBy"));
        outboundService.confirm(id, trackingNo, confirmedBy);
        return Result.success();
    }

    @GetMapping
    public Result<List<Outbound>> list(@RequestParam(required = false) String outboundNo,
                                       @RequestParam(required = false) String status) {
        return Result.success(outboundService.list(outboundNo, status));
    }
}
