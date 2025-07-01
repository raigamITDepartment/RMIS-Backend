package com.company.finance.finance_manager.controller;

import com.company.finance.finance_manager.dto.*;
import com.company.finance.finance_manager.enums.ERole;
import com.company.finance.finance_manager.entity.Role;
import com.company.finance.finance_manager.entity.User;
import com.company.finance.finance_manager.repository.RoleRepository;
import com.company.finance.finance_manager.repository.UserRepository;
import com.company.finance.finance_manager.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;


    @PostMapping("/login/")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken((UserDetails) authentication.getPrincipal());

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponse(user));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        String username = jwtUtils.getUserNameFromJwtToken(token);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // Convert roles to a set of role names
        Set<ERole> roleNames = user.getRoles()
                .stream()
                .map(Role::getName) // Assuming getName() returns something like "ROLE_USER"
                .collect(Collectors.toSet());

        // Map User to MeDTO
        MeDTO meDTO = new MeDTO(user.getFirstName(), user.getLastName(), user.getEmail(), roleNames);
        return ResponseEntity.ok(meDTO); // You might want to return a DTO instead of the full User entity
    }
}
