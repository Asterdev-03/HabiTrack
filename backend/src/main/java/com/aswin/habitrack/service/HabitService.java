package com.aswin.habitrack.service;

import com.aswin.habitrack.dto.HabitRequest;
import com.aswin.habitrack.dto.HabitResponse;
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
    public List<HabitResponse> getHabitsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return habitRepository
                .findByUser(user)
                .stream()
                .map(h -> new HabitResponse(h.getId(), h.getName(), h.getDescription()))
                .toList();
    }

    // Add a new habit for a user
    public HabitResponse createHabitForUser(HabitRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Habit habit = new Habit();
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setUser(user);
        Habit saved = habitRepository.save(habit);
        return new HabitResponse(saved.getId(), saved.getName(), saved.getDescription());
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
