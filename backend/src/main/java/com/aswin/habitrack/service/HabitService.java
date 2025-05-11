package com.aswin.habitrack.service;

import com.aswin.habitrack.model.Habit;
import com.aswin.habitrack.model.User;
import com.aswin.habitrack.repository.HabitRepository;
import com.aswin.habitrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    public HabitService(HabitRepository habitRepository, UserRepository userRepository) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
    }

    // Fetch habits for a user
    public List<Habit> getHabitsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return habitRepository.findByUser(user);
    }

    // Add a new habit for a user
    public Habit createHabitForUser(Habit habit, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        habit.setUser(user);
        return habitRepository.save(habit);
    }

    // Delete a habit
    public void deleteHabit(Long id, String username) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
        if (!habit.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own habits");
        }
        habitRepository.delete(habit);
    }
}
