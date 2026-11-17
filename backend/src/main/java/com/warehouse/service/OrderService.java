package com.warehouse.service;

import com.warehouse.entity.Order;
import com.warehouse.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public Order create(Order order) {
        orderMapper.insert(order);
        return order;
    }

    public List<Order> list(String orderNo, String status) {
        return orderMapper.findAll(orderNo, status);
    }

    public Order getById(Long id) {
        return orderMapper.findById(id);
    }

    public void ship(Long id) {
        orderMapper.ship(id);
    }
}
