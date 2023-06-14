package com.dqt.positionservice.dto;

import com.dqt.positionservice.model.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagePositionDTO {
    private List<Position> content;
    private Pageable pageable;




}
