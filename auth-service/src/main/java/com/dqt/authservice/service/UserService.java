package com.dqt.authservice.service;

import com.dqt.authservice.dto.request.SignUpForm;
import com.dqt.authservice.entity.Role;
import com.dqt.authservice.entity.User;
import com.dqt.authservice.exception.ResourceNotFoundException;
import com.dqt.authservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtSecret = "dqt";


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    public User findById(Long id) {
        log.info("Get User from Database!");
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id + ""));
    }


    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User signUp(SignUpForm signUpForm) {
        User user = new User(signUpForm.getName(), signUpForm.getEmail(), passwordEncoder.encode(signUpForm.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleService.findByName("USER").orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", "role"));
        roles.add(userRole);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
