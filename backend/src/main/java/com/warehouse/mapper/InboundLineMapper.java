package com.warehouse.mapper;

import com.warehouse.entity.InboundLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * WLS-05 入库明细Mapper骨架。SQL实现由该任务负责人补充。
 */
@Mapper
public interface InboundLineMapper {

    // TODO(WLS-05): SELECT * FROM inbound_line WHERE inbound_id = #{inboundId}
    List<InboundLine> findByInboundId(Long inboundId);

    // TODO(WLS-05): INSERT，返回自增id
    int insert(InboundLine line);

    // TODO(WLS-05): DELETE FROM inbound_line WHERE id = #{id}
    int deleteById(Long id);
}
