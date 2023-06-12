package com.dqt.positionservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PositionDTO {
    private Long id;
    private String name;

    private List<Employee> employees = new ArrayList<>();

}
