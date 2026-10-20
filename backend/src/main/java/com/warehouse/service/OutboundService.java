package com.warehouse.service;

import com.warehouse.entity.Outbound;
import com.warehouse.mapper.OutboundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboundService {

    @Autowired
    private OutboundMapper outboundMapper;

    public List<Outbound> list() {
        return outboundMapper.findAll();
    }

    public Outbound getById(Long id) {
        return outboundMapper.findById(id);
    }

    public void save(Outbound outbound) {
        if (outbound.getId() == null) {
            outboundMapper.insert(outbound);
        } else {
            outboundMapper.update(outbound);
        }
    }

    public void delete(Long id) {
        outboundMapper.deleteById(id);
    }
}
