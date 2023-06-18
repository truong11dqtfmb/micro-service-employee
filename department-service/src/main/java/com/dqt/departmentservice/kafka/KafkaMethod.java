package com.dqt.departmentservice.kafka;

import com.dqt.departmentservice.dto.Employee;
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
