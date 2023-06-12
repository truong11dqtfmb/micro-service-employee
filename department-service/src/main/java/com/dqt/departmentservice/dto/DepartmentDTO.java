package com.dqt.departmentservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DepartmentDTO {
    private Long id;
    private String name;

    private List<Employee> employees = new ArrayList<>();

}
