package com.dqt.authservice.dto.response;

import com.dqt.authservice.entity.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String name;
    private Set<Role> roles;

//    private Collection<? extends GrantedAuthority> roles;
//
//    public JwtResponse(String refreshToken, String token, String name, Collection<? extends GrantedAuthority> authorities) {
//        this.refreshToken = refreshToken;
//        this.token = token;
//        this.name = name;
//        this.roles = authorities;
//    }


    public JwtResponse(String jwt, String token, String email, Set<Role> roles) {
        this.refreshToken = jwt;
        this.token = token;
        this.name = email;
        this.roles = roles;
    }
}
