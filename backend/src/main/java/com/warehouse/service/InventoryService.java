package com.warehouse.service;

import com.warehouse.entity.Inventory;
import com.warehouse.mapper.InventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    public List<Inventory> list() {
        return inventoryMapper.findAll();
    }

    public Inventory getById(Long id) {
        return inventoryMapper.findById(id);
    }

    public void save(Inventory inventory) {
        if (inventory.getId() == null) {
            inventoryMapper.insert(inventory);
        } else {
            inventoryMapper.update(inventory);
        }
    }

    public void delete(Long id) {
        inventoryMapper.deleteById(id);
    }
}
