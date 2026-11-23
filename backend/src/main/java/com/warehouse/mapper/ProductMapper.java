package com.warehouse.mapper;

import com.warehouse.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * WLS-04 商品Mapper骨架。
 * 方法签名已定好，SQL实现由该任务负责人补充（参考InventoryMapper的注解写法）。
 */
@Mapper
public interface ProductMapper {

    // TODO(WLS-04): SELECT * FROM product WHERE id = #{id}
    Product findById(Long id);

    // TODO(WLS-04): SELECT * FROM product ORDER BY id，enabled过滤按需加
    List<Product> findAll(Boolean enabled);
}
