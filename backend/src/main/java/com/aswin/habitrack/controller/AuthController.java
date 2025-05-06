package com.aswin.habitrack.controller;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aswin.habitrack.model.AuthRequest;
import com.aswin.habitrack.model.AuthResponse;
import com.aswin.habitrack.model.User;
import com.aswin.habitrack.repository.UserRepository;
import com.aswin.habitrack.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest) {
        if (userRepo.findByUsername(authRequest.getUsername()).isPresent())
            return "Username already exist";

        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        userRepo.save(user);

        return "User registered succesfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        Optional<User> userOpt = userRepo.findByUsername(authRequest.getUsername());

        if (userOpt.isPresent() && passwordEncoder.matches(authRequest.getPassword(), userOpt.get().getPassword())) {
            String token = jwtUtil.generateToken(authRequest.getUsername());
            System.out.println("logged in");
            return new AuthResponse(token);
        }
        throw new RuntimeException();
    }
}
