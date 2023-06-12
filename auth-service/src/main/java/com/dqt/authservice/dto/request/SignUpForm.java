package com.dqt.authservice.dto.request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpForm {
    private String name;
    private String email;
    private String password;
    private Set<String> roles;
}
