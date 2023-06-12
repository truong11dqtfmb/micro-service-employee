package com.dqt.authservice.security.custom;

import com.dqt.authservice.entity.User;
import com.dqt.authservice.exception.ResourceNotFoundException;
import com.dqt.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            System.out.println("User not found");
            throw new ResourceNotFoundException("User","user",email);
        } else {

            List<GrantedAuthority> authorities = user.get().getRoles().stream().map(role ->
                    new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

            log.info("User Detail: " + user.get().getEmail() + " - " + authorities);
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
        }
    }
}

