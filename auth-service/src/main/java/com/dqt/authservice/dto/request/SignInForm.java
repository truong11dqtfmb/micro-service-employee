package com.dqt.authservice.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInForm {
    private String email;
    private String password;
}

