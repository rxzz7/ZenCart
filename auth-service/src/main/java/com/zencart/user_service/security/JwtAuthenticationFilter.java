//package com.zencart.user_service.security;
//
//import com.zencart.user_service.auth.service.serviceimpl.CustomUserDetailsService;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final CustomUserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String header = request.getHeader("Authorization");
//        if(header != null && header.startsWith("Bearer ")){
//            String token = header.substring(7);
//            try {
//                String username = jwtUtil.extractUsername(token);
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//                    UserDetails details = userDetailsService.loadUserByUsername(username);
//                    if (jwtUtil.validateToken(token, details.getUsername()) && details.isEnabled()){
//                        System.out.println("Authorities: " + details.getAuthorities());
//                        SecurityContextHolder.getContext().setAuthentication(
//                                new UsernamePasswordAuthenticationToken(details.getUsername(), null, details.getAuthorities())
//                        );
//                    }
//                }
//            }catch (JwtException | IllegalArgumentException ex){
//                SecurityContextHolder.clearContext();
//
//            }
//        }
//        filterChain.doFilter(request, response);
//
//    }
//}





//ORIGINAL -> UPDATED -> JWKS
