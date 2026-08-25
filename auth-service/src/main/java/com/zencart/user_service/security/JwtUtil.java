package com.zencart.user_service.security;

public interface JwtUtil {

//    String extractUsername(String token);
//    boolean validateToken(String token, String username);

    String generateToken(String username, String name);
}
