package com.warehouse.controller;

import com.warehouse.entity.Order;
import com.warehouse.service.OrderService;
import com.warehouse.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result<Order> create(@RequestBody Map<String, String> params) {
        Order order = new Order();
        order.setOrderNo(params.get("orderNo"));
        order.setCustomerName(params.get("customerName"));
        order.setProductId(Long.valueOf(params.get("productId")));
        order.setQuantity(Integer.valueOf(params.get("quantity")));
        order.setCreatedBy(Long.valueOf(params.get("createdBy")));
        return Result.success(orderService.create(order));
    }

    @GetMapping
    public Result<List<Order>> list(@RequestParam(required = false) String orderNo,
                                    @RequestParam(required = false) String status) {
        return Result.success(orderService.list(orderNo, status));
    }
}
