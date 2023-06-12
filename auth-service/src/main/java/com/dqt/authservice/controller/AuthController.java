package com.dqt.authservice.controller;

import com.dqt.authservice.dto.request.SignInForm;
import com.dqt.authservice.dto.request.SignUpForm;
import com.dqt.authservice.dto.response.ApiResponse;
import com.dqt.authservice.entity.User;
import com.dqt.authservice.security.jwt.JwtService;
import com.dqt.authservice.service.RoleService;
import com.dqt.authservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> singup(@RequestBody SignUpForm signUpForm) {
        ApiResponse apiResponse = new ApiResponse();


        if (userService.existsByEmail(signUpForm.getEmail())) {
            apiResponse.setStatus(false);
            apiResponse.setMessage("The email existed! Please try again");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        User user = this.userService.signUp(signUpForm);
        apiResponse.setStatus(true);
        apiResponse.setMessage("Create Account successfully!");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> singin(@RequestBody SignInForm signInForm) throws JsonProcessingException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInForm.getEmail(),signInForm.getPassword()));

        System.out.println("Authenticate");

        if (authentication.isAuthenticated()) {

            User user = this.userService.findByEmail(signInForm.getEmail());

            String access_token = jwtService.generateToken(signInForm.getEmail());

//      return API
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
//            tokens.put("refresh_token", refresh_token);
            tokens.put("email", signInForm.getEmail());
            tokens.put("roles", user.getRoles().stream().collect(Collectors.toList()));

            String json = new ObjectMapper().writeValueAsString(tokens);
            return new ResponseEntity<>(json, OK);


        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO refreshToken) throws IOException {
//        try {
//
//            String refresh_token = refreshToken.getRefreshToken();
//
//            Algorithm algorithmAccess = Algorithm.HMAC256(jwtSecret.getBytes());
//            Algorithm algorithmRefresh = Algorithm.HMAC256(jwtRefreshSecret.getBytes());
//
////          Verify & DecodeJWT   => Email
//            JWTVerifier verifier = JWT.require(algorithmRefresh).build();
//            DecodedJWT decodedJWT = verifier.verify(refresh_token);
//            String email = decodedJWT.getSubject();
//
//            User u = userService.findByEmail(email);
//            User user = userService.findById(u.getId());
//
//            CustomUserDetail customUserDetail = CustomUserDetail.build(user);
//
////          Tạo access_token mới
//            String access_token = JWT.create()
//                    .withSubject(user.getEmail())
//                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
////                        .withIssuer(request.getRequestURL().toString())
////                        .withClaim("roles", user.getRoles().stream().collect(Collectors.toList()))
//                    .sign(algorithmAccess);
//
//
////          Return Api
//            Map<String, Object> tokens = new HashMap<>();
//            tokens.put("access_token", access_token);
//            tokens.put("refresh_token", refresh_token);
//            tokens.put("email", customUserDetail.getUsername());
//            tokens.put("roles", customUserDetail.getAuthorities().stream().collect(Collectors.toList()));
//
//            String json = new ObjectMapper().writeValueAsString(tokens);
//
//            return new ResponseEntity<>(json, OK);
//
//
//        } catch (Exception exception) {
//
//            log.info("loi truy cap vao refresh token");
//            String error = exception.getMessage();
//            return new ResponseEntity<>(error, UNAUTHORIZED);
//        }
//    }


//    @PostMapping("/refresh")
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                String refresh_token = authorizationHeader.substring("Bearer ".length());
//                Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
//                Algorithm algorithmAccess = Algorithm.HMAC256(jwtSecret.getBytes());
//                Algorithm algorithmRefresh = Algorithm.HMAC256(jwtRefreshSecret.getBytes());
//                JWTVerifier verifier = JWT.require(algorithmRefresh).build();
//                DecodedJWT decodedJWT = verifier.verify(refresh_token);
//                String email = decodedJWT.getSubject();
//
//                User user = userService.findByEmail(email);
//
////                org.springframework.security.core.userdetails.User useroke = user;
//
//                CustomUserDetail customUserDetail = CustomUserDetail.build(user);
//
//
////                List<Role> rolesss =  customUserDetail.getAuthorities().stream().map(Role ->{
////                    String e =  com.dqt.token.entities.Role.class.getName();
////                    RoleName roleName = RoleName.valueOf(String.valueOf(e));
////
////                    String name = roleName.name();
////
////                    return name;
////                }).collect(Collectors.toList());
//
//                String access_token = JWT.create()
//                        .withSubject(user.getEmail())
//                        .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
////                        .withIssuer(request.getRequestURL().toString())
////                        .withClaim("roles", user.getRoles().stream().collect(Collectors.toList()))
//                        .sign(algorithmAccess);
//
//
//
//
////                .withClaim("roles", customUserDetail.getAuthorities().stream().collect(Collectors.toList()).toString())
//
////                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//
//
//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("access_token", access_token);
//                tokens.put("refresh_token", refresh_token);
//                tokens.put("email", customUserDetail.getUsername());
//                tokens.put("roles", customUserDetail.getAuthorities().stream().collect(Collectors.toList()).toString());
//
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//            } catch (Exception exception) {
//                response.setHeader("error", exception.getMessage());
//                response.setStatus(FORBIDDEN.value());
//                //response.sendError(FORBIDDEN.value());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", exception.getMessage());
//                log.info("loi truy cap vao refresh token");
//                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }
//        } else {
//            throw new RuntimeException("Refresh token is missing");
//        }
//    }


}



