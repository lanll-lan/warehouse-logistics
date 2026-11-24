package com.warehouse.service;

import com.warehouse.entity.StockMovement;
import com.warehouse.mapper.StockMovementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMovementService {

    @Autowired
    private StockMovementMapper stockMovementMapper;

    public List<StockMovement> list(Long inventoryId, String movementType) {
        List<StockMovement> all = stockMovementMapper.findAll();
        boolean hasInventory = inventoryId != null;
        boolean hasType = movementType != null && !movementType.isEmpty();
        if (hasInventory && hasType) {
            return all.stream()
                    .filter(m -> inventoryId.equals(m.getInventoryId()) && movementType.equals(m.getMovementType()))
                    .collect(Collectors.toList());
        } else if (hasInventory) {
            return all.stream()
                    .filter(m -> inventoryId.equals(m.getInventoryId()))
                    .collect(Collectors.toList());
        } else if (hasType) {
            return all.stream()
                    .filter(m -> movementType.equals(m.getMovementType()))
                    .collect(Collectors.toList());
        }
        return all;
    }
}
