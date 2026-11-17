package com.warehouse.service;

import com.warehouse.entity.Order;
import com.warehouse.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public Order create(Order order) {
        orderMapper.insert(order);
        return order;
    }

    public List<Order> list(String orderNo, String status) {
        List<Order> all = orderMapper.findAll();
        if (orderNo != null && !orderNo.isEmpty() && status != null && !status.isEmpty()) {
            return all.stream()
                    .filter(o -> o.getOrderNo().contains(orderNo) && o.getStatus().equals(status))
                    .collect(Collectors.toList());
        } else if (orderNo != null && !orderNo.isEmpty()) {
            return all.stream()
                    .filter(o -> o.getOrderNo().contains(orderNo))
                    .collect(Collectors.toList());
        } else if (status != null && !status.isEmpty()) {
            return all.stream()
                    .filter(o -> o.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        return all;
    }

    public Order getById(Long id) {
        return orderMapper.findById(id);
    }

    public void ship(Long id) {
        orderMapper.ship(id);
    }
}