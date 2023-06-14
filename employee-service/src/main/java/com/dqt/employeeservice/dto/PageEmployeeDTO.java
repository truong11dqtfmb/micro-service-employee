package com.dqt.employeeservice.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageEmployeeDTO {
    private List<EmployeeDTO> content;
    private Pageable pageable;




}
