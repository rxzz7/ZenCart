package com.zencart.order_service.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class JwtTokenFilter {

    private JwtTokenFilter(){}

    public static String getTokenFromRequest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)){
            return "";
        }
        return jwtAuthenticationToken.getToken().getTokenValue();
    }
}
