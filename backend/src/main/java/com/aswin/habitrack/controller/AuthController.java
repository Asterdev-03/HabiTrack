package com.aswin.habitrack.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aswin.habitrack.dto.AuthRequest;
import com.aswin.habitrack.dto.AuthResponse;
import com.aswin.habitrack.exception.UnauthorizedException;
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
        User user = userRepo.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
