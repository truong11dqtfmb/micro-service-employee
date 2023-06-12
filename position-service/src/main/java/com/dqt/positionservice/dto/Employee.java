package com.dqt.positionservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {
    private Long id;
    private Long departmentId;
    private String name;
    private int age;
    private Long positionId;
}
