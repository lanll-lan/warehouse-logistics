package com.warehouse.mapper;

import com.warehouse.entity.Inbound;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * WLS-05 入库单Mapper骨架。SQL实现由该任务负责人补充。
 */
@Mapper
public interface InboundMapper {

    // TODO(WLS-05): SELECT * FROM inbound WHERE id = #{id}
    Inbound findById(Long id);

    // TODO(WLS-05): SELECT * FROM inbound ORDER BY created_at DESC，status过滤按需加
    List<Inbound> findAll(String status);

    // TODO(WLS-05): INSERT，status默认DRAFT，返回自增id
    int insert(Inbound inbound);

    // TODO(WLS-05): UPDATE inbound SET status='CONFIRMED', confirmed_by=#{confirmedBy}, confirmed_at=NOW() WHERE id=#{id} AND status='DRAFT'
    int confirm(Long id, Long confirmedBy);
}
