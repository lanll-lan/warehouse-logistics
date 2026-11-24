package com.warehouse.service;

import com.warehouse.entity.Product;
import com.warehouse.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public List<Product> list(Boolean enabled) {
        return productMapper.findAll(enabled);
    }

    public Product getById(Long id) {
        Product product = productMapper.findById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        return product;
    }
}
