package com.warehouse.mapper;

import com.warehouse.entity.StockCount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * WLS-06 盘点Mapper骨架。SQL实现由该任务负责人补充。
 */
@Mapper
public interface StockCountMapper {

    // TODO(WLS-06): SELECT * FROM stock_count WHERE id = #{id}
    StockCount findById(Long id);

    // TODO(WLS-06): SELECT * FROM stock_count ORDER BY counted_at DESC
    List<StockCount> findAll(Long inventoryId);

    // TODO(WLS-06): INSERT，systemQty/stockVersion由后端查当前库存填入，返回自增id
    int insert(StockCount stockCount);
}
