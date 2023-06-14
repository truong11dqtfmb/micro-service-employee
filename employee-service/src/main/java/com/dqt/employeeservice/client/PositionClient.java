package com.dqt.employeeservice.client;

import com.dqt.employeeservice.dto.Position;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface PositionClient {
    @GetExchange("/api/position/{id}")
    Position findById(@PathVariable Long id);

    @GetExchange("/api/position/client")
    List<Position> findAllClient();

}
