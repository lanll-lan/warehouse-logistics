package com.warehouse.mapper;

import com.warehouse.entity.LogisticsEvent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 物流事件Mapper。牵头人写完整，WLS-07 直接复用，不要改这里。
 */
@Mapper
public interface LogisticsEventMapper {

    @Select("SELECT * FROM logistics_event WHERE id = #{id}")
    LogisticsEvent findById(Long id);

    // 幂等用：按外部事件号查重
    @Select("SELECT * FROM logistics_event WHERE external_event_id = #{externalEventId}")
    LogisticsEvent findByExternalEventId(String externalEventId);

    @Select("SELECT * FROM logistics_event WHERE outbound_id = #{outboundId} ORDER BY event_time DESC")
    List<LogisticsEvent> findByOutboundId(Long outboundId);

    @Insert("INSERT INTO logistics_event (external_event_id, outbound_id, status, event_time) " +
            "VALUES (#{externalEventId}, #{outboundId}, #{status}, #{eventTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LogisticsEvent event);
}
