package com.aswin.habitrack.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aswin.habitrack.model.Habit;
import com.aswin.habitrack.model.User;
import com.aswin.habitrack.repository.HabitRepository;
import com.aswin.habitrack.repository.UserRepository;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitRepository habitRepo;
    private final UserRepository userRepo;

    public HabitController(UserRepository userRepository, HabitRepository habitRepository) {
        this.habitRepo = habitRepository;
        this.userRepo = userRepository;
    }

    @PostMapping
    public Habit createHabit(@RequestBody Habit habit, Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).orElseThrow();
        habit.setUser(user);
        return habitRepo.save(habit);
    }

    @GetMapping
    public List<Habit> getHabits(Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).orElseThrow();
        return habitRepo.findByUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id, Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).orElseThrow();
        Habit habit = habitRepo.findById(id).orElseThrow();

        // Checks if the habit’s user.id == current user.id before deleting
        // prevents others from deleting your habits
        if (habit.getUser().getId().equals(user.getId())) {
            habitRepo.delete(habit);
        }
    }
}
