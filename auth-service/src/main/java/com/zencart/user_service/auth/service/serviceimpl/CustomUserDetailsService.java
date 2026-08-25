package com.zencart.user_service.auth.service.serviceimpl;


import com.zencart.user_service.entity.Credential;
import com.zencart.user_service.repo.CredentialRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CredentialRepo credentialRepo;

    public CustomUserDetailsService(CredentialRepo credentialRepo){
        this.credentialRepo = credentialRepo;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Credential credential =  credentialRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with name %s"+ username));


        //Spring security's User object
        return User.withUsername(credential.getUsername())
                .password(credential.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(credential.getRoleBasedAuthority().name())))
                .disabled(!Boolean.TRUE.equals(credential.getIsEnabled()))
                .accountLocked(!Boolean.TRUE.equals(credential.getIsAccountNonLocked()))
                .accountExpired(!Boolean.TRUE.equals(credential.getIsAccountNonExpired()))
                .credentialsExpired(!Boolean.TRUE.equals(credential.getIsCredentialsNonExpired()))
                .build();
    }
}
