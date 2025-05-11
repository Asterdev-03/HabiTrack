package com.aswin.habitrack.controller;

import com.aswin.habitrack.dto.HabitRequest;
import com.aswin.habitrack.dto.HabitResponse;
import com.aswin.habitrack.service.HabitService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    // Create habit
    @PostMapping
    public HabitResponse createHabit(@RequestBody @Valid HabitRequest habitRequest, Authentication auth) {
        return habitService.createHabitForUser(habitRequest, auth.getName());
    }

    // Get habits
    @GetMapping
    public List<HabitResponse> getHabits(Authentication auth) {
        return habitService.getHabitsForUser(auth.getName());
    }

    // Delete habit
    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id, Authentication auth) {
        habitService.deleteHabit(id, auth.getName());
    }
}
