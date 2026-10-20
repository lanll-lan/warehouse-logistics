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

    public List<Order> list() {
        return orderMapper.findAll();
    }

    public Order getById(Long id) {
        return orderMapper.findById(id);
    }

    public void save(Order order) {
        if (order.getId() == null) {
            orderMapper.insert(order);
        } else {
            orderMapper.update(order);
        }
    }

    public void delete(Long id) {
        orderMapper.deleteById(id);
    }
}
