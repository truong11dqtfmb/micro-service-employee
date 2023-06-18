package com.dqt.employeeservice.kafka;

import com.dqt.employeeservice.model.Employee;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KafkaMethod {
    private String method;
    private Long id;
    private Employee employee = null;
}
