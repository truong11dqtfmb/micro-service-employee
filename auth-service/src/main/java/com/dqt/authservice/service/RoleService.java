package com.dqt.authservice.service;

import com.dqt.authservice.entity.Role;
import com.dqt.authservice.exception.ResourceNotFoundException;
import com.dqt.authservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    public Optional<Role> findByName(String name) {
        return Optional.ofNullable(roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", name.toString())));
    }
}

