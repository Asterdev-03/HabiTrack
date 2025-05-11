package com.aswin.habitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HabitResponse {
    private Long id;
    private String name;
    private String description;

}
