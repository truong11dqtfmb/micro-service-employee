package com.dqt.positionservice.kafka;

import com.dqt.positionservice.dto.Employee;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KafkaMethod {
    private String method;
    private Long id;
    private Employee employee;
}
