package com.aswin.habitrack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitRequest {

    @NotBlank(message = "Habit name is required")
    private String name;

    @NotBlank(message = "Habit description is required")
    private String description;
}
