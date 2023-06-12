package com.dqt.employeeservice.dto;

import com.dqt.employeeservice.dto.Department;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeDTO {
    private Long id;
    private Department department;
//    private Long departmentId;
    private String name;
    private int age;
    private Position position;
//    private Long positionId;
}
