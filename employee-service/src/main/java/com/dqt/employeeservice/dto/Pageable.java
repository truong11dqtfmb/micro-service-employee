package com.dqt.employeeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pageable {
    private long totalPages;
    private long totalElements;
    private long size;
    private long currentPage;
}
