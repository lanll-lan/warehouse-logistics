package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Outbound;
import com.warehouse.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outbound")
public class OutboundController {

    @Autowired
    private OutboundService outboundService;

    @GetMapping("/list")
    public Result<List<Outbound>> list() {
        return Result.success(outboundService.list());
    }

    @GetMapping("/{id}")
    public Result<Outbound> getById(@PathVariable Long id) {
        return Result.success(outboundService.getById(id));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Outbound outbound) {
        outboundService.save(outbound);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        outboundService.delete(id);
        return Result.success();
    }
}
