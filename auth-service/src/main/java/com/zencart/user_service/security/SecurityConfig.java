package com.zencart.user_service.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

//    private JwtAuthenticationFilter jwtAuthenticationFilter;

//    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter().write("""
                        {"timestamp":"%s",
                        "status":401,
                        "error":"UNAUTHORIZED",
                        "message":"Authentication is required",
                        "path":"%s"}
                        """.formatted(Instant.now(), req.getRequestURI()));
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.getWriter().write("""
                        {"timestamp":"%s",
                        "status":403,
                        "error":"FORBIDDEN",
                        "message":"Access is denied",
                        "path":"%s"}
                        """.formatted(Instant.now(), req.getRequestURI()));
                        }))
                .authorizeHttpRequests(auth -> auth
                        //Swagger or OpenAPI docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        //Public endpoints
                        .requestMatchers(
                                "/api/auth/activate",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password",
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/refresh",
                                "/.well-known/jwks.json"
                        ).permitAll()

                        .anyRequest().authenticated()
                );
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    "Take Spring Security's already-configured AuthenticationManager and register it as a
//    Spring bean, so I can inject and use it elsewhere in my application."
    //********************   *******************
//AuthenticationConfiguration is a Spring Security class that holds/accesses the
// authentication setup that Spring has built for your application.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }


//
//            ┌──────────────────────────────────────────┐
//            │              Spring Context              │
//            │                                          │
//            │  CustomUserDetailsService                │
//            │          │                               │
//            │          │ implements                    │
//            │          ▼                               │
//            │  UserDetailsService                      │
//            │                                          │
//            │  BCryptPasswordEncoder                   │
//            │          │                               │
//            │          ▼                               │
//            │  PasswordEncoder                         │
//            │                                          │
//            │  AuthenticationManager                   │
//            │          │                               │
//            │          ▼                               │
//            │  AuthenticationConfiguration             │
//            └──────────────────────────────────────────┘
//    "Spring Security, give me the AuthenticationManager that you have already configured using
//    the authentication components available in the application."
}
