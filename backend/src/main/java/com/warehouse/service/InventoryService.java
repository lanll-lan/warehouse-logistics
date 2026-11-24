package com.warehouse.service;

import com.warehouse.entity.Inventory;
import com.warehouse.mapper.InventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    public List<Inventory> list(Long productId, Long locationId) {
        List<Inventory> all = inventoryMapper.findAll();
        if (productId != null && locationId != null) {
            return all.stream()
                    .filter(inv -> inv.getProductId().equals(productId) && inv.getLocationId().equals(locationId))
                    .collect(Collectors.toList());
        } else if (productId != null) {
            return all.stream()
                    .filter(inv -> inv.getProductId().equals(productId))
                    .collect(Collectors.toList());
        } else if (locationId != null) {
            return all.stream()
                    .filter(inv -> inv.getLocationId().equals(locationId))
                    .collect(Collectors.toList());
        }
        return all;
    }

    public Inventory getById(Long id) {
        return inventoryMapper.findById(id);
    }

    @Transactional
    public void adjust(Long inventoryId, Integer delta, Long operatorId) {
        Inventory inv = inventoryMapper.findById(inventoryId);
        if (inv == null) throw new RuntimeException("库存记录不存在");
        int updated = inventoryMapper.updateQuantity(inventoryId, delta);
        if (updated == 0) throw new RuntimeException("并发冲突，请重试");
    }

    @Transactional
    public void adjustByProductLocation(Long productId, Long locationId, Integer quantity, Long operatorId) {
        Inventory inv = inventoryMapper.findByProductAndLocation(productId, locationId);
        if (inv == null) {
            inv = new Inventory();
            inv.setProductId(productId);
            inv.setLocationId(locationId);
            inv.setQuantity(0);
            inventoryMapper.insert(inv);
            inv = inventoryMapper.findByProductAndLocation(productId, locationId);
        }
        int updated = inventoryMapper.updateQuantity(inv.getId(), quantity);
        if (updated == 0) throw new RuntimeException("并发冲突，请重试");
    }

    @Transactional
    public void setQuantity(Long inventoryId, Integer quantity, Long version) {
        int updated = inventoryMapper.setQuantity(inventoryId, quantity);
        if (updated == 0) throw new RuntimeException("并发冲突，请重试");
    }
}