package com.company.finance.finance_manager.service;

import com.company.finance.finance_manager.dto.UserDTO;
import com.company.finance.finance_manager.enums.ERole;
import com.company.finance.finance_manager.entity.Role;
import com.company.finance.finance_manager.entity.User;
import com.company.finance.finance_manager.exception.BadRequestException;
import com.company.finance.finance_manager.exception.ResourceNotFoundException;
import com.company.finance.finance_manager.repository.RoleRepository;
import com.company.finance.finance_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    public void initRole() {

        if (!roleRepository.existsByName(ERole.ROLE_ADMIN)) {
            Role adminRole = new Role();
            adminRole.setName(ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);
        }

        if (!roleRepository.existsByName(ERole.ROLE_FINISH_GOOD)) {
            Role fgRole = new Role();
            fgRole.setName(ERole.ROLE_FINISH_GOOD);
            roleRepository.save(fgRole);
        }
        if (!roleRepository.existsByName(ERole.ROLE_FINISH_GOOD_HEAD)) {
            Role fgHeadRole = new Role();
            fgHeadRole.setName(ERole.ROLE_FINISH_GOOD_HEAD);
            roleRepository.save(fgHeadRole);
        }

        if (!roleRepository.existsByName(ERole.ROLE_FINANCE)) {
            Role financeRole = new Role();
            financeRole.setName(ERole.ROLE_FINANCE);
            roleRepository.save(financeRole);
        }

        if (!roleRepository.existsByName(ERole.ROLE_FINANCE_HEAD)) {
            Role financeHeadRole = new Role();
            financeHeadRole.setName(ERole.ROLE_FINANCE_HEAD);
            roleRepository.save(financeHeadRole);
        }
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BadRequestException("User with email '" + userDTO.getEmail() + "' already exists.");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(encoder.encode(userDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(userDTO.getRole())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User updateUser(Integer id, UserDTO updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));

        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());
        user.setEmail(updateDto.getEmail());
        user.setUsername(updateDto.getUsername());

        // Only update password if a new one is provided (optional, but recommended)
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(updateDto.getPassword()));
        }

        // Update role
        Set<Role> roles = new HashSet<>();
        Role newRole = roleRepository.findByName(updateDto.getRole())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(newRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

}
