package com.dqt.departmentservice.dto;

import com.dqt.departmentservice.model.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageDepartmentDTO {
    private List<Department> content;
    private Pageable pageable;




}
