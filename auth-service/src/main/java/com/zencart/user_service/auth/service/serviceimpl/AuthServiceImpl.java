package com.zencart.user_service.auth.service.serviceimpl;

import com.zencart.user_service.auth.activation.ActivateRequest;
import com.zencart.user_service.auth.dto.MessageResponse;
import com.zencart.user_service.auth.login.LoginRequest;
import com.zencart.user_service.auth.passwordreset.ForgotPasswordRequest;
import com.zencart.user_service.auth.passwordreset.ResetPasswordRequest;
import com.zencart.user_service.auth.registration.RegisterRequest;
import com.zencart.user_service.auth.service.AuthService;
import com.zencart.user_service.auth.service.EmailService;
import com.zencart.user_service.auth.token.RefreshTokenRequest;
import com.zencart.user_service.auth.token.TokenResponse;
import com.zencart.user_service.entity.*;
import com.zencart.user_service.exception.ConflictException;
import com.zencart.user_service.exception.InvalidTokenException;
import com.zencart.user_service.exception.UnauthorizedException;
import com.zencart.user_service.repo.CredentialRepo;
import com.zencart.user_service.repo.RefreshTokenRepo;
import com.zencart.user_service.repo.UserRepo;
import com.zencart.user_service.repo.VerificationTokenRepo;
import com.zencart.user_service.security.JwtUtilImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Duration REFRESH_TTL = Duration.ofDays(30);
    private static final Duration ACTIVATION_TTL = Duration.ofHours(24);
    private static final Duration RESET_TTL = Duration.ofHours(1);


    private final UserRepo userRepo;
    private final CredentialRepo credentialRepo;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepo verificationTokenRepo;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtilImpl jwtUtil;
    private final RefreshTokenRepo refreshTokenRepo;


    @Override
    @Transactional
    public MessageResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        String username = request.firstName().trim().toLowerCase();
        if(userRepo.existsByEmailIgnoreCase(email)){
            throw new ConflictException("Email is already registered");
        }
        if(credentialRepo.existsByUsername(username)){
            throw new ConflictException("Username already exists");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(email)
                .phone(request.phone())
                .build();

        Credential credential =Credential.builder()
                .username(username)
                .password(passwordEncoder.encode(request.password()))
                .roleBasedAuthority(RoleBasedAuthority.ROLE_USER)
                .isEnabled(false)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .user(user)
                .build();

        user.setCredential(credential);
        User savedUser = userRepo.save(user);

        String activationToken =createVerificationToken(savedUser.getCredential(),TokenPurpose.ACTIVATION,ACTIVATION_TTL);
        emailService.sendSimpleMessage(email, "Activate your ZenCart account",
                "Activate your account with this token: " + activationToken);

        // Registration creates the account but does not authenticate an unactivated user.
        return new MessageResponse("Registration successful. Check your email to activate the account");
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        //authenticationManager is an exposed bean in SecurityConfig || it asks for name and password for authentication
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(request.username().trim().toLowerCase(),
                            request.password())
            );
        }catch (AuthenticationException exception){
            throw new UnauthorizedException("Invalid username or password");
        }
        Credential credential = credentialRepo.findByUsername(request.username().trim().toLowerCase())
                .orElseThrow(()-> new UnauthorizedException("Invalid Username or password"));
        String accessToken = jwtUtil.generateToken(credential.getUsername(), credential.getRoleBasedAuthority().name());
        String refreshToken = createRefreshToken(credential);
        return new TokenResponse(accessToken, refreshToken, "Bearer", jwtUtil.getExpiration() / 1000);
    }

    @Override
    public TokenResponse refresh(RefreshTokenRequest request) {
        RefreshToken token = refreshTokenRepo.findByTokenAndRevokedFalse(request.refreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        if (token.getExpiresAt().isBefore(Instant.now())){
            token.setRevoked(true);
            throw new UnauthorizedException("Token has expired");
        }

        token.setRevoked(true);
        Credential credential = token.getCredential();
        if (!Boolean.TRUE.equals(credential.getIsEnabled())){
            throw new UnauthorizedException("Account is not activated");
        }
        String accessToken = jwtUtil.generateToken(credential.getUsername(), credential.getRoleBasedAuthority().name());
        String refreshToken = createRefreshToken(credential);
        return new TokenResponse(accessToken, refreshToken, "Bearer", jwtUtil.getExpiration() / 1000);
    }

    //This is to proof that the given email belongs to the user
    @Override
    public MessageResponse activate(ActivateRequest request) {
        VerificationToken token = verificationTokenRepo
                .findByTokenAndPurpose(request.token(), TokenPurpose.ACTIVATION)
                .orElseThrow(()-> new InvalidTokenException("Invalid verification Token"));
        if(token.getExpireDate().isBefore(LocalDateTime.now())){
            throw  new InvalidTokenException("Verification token is expired");
        }

        Credential credential = token.getCredential();
        credential.setIsEnabled(true);
        token.setToken(null);
        verificationTokenRepo.delete(token);
        return new MessageResponse("Account activated successfully");
    }

    @Override
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        userRepo.findByEmailIgnoreCase(request.email().trim().toLowerCase()).ifPresent(user -> {
            Credential credential = user.getCredential();
            if(credential != null){
                String token = createVerificationToken(credential,TokenPurpose.PASSWORD_RESET, RESET_TTL);
                emailService.sendSimpleMessage(user.getEmail(), "RESET YOUR ZENCART PASSWORD",
                        "Use this token to reset your password: " + token);
            }
        });
        //  Same response whether the account exists or not prevents email enumeration.
        return new MessageResponse("If the email is registered, a password reset token has been sent");
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        VerificationToken verificationToken = verificationTokenRepo
                .findByTokenAndPurpose(request.token(), TokenPurpose.PASSWORD_RESET)
                .orElseThrow(() -> new UnauthorizedException("Invalid password reset token"));
        Credential credential = verificationToken.getCredential();
        credential.setPassword(passwordEncoder.encode(request.newPassword()));
        credential.setIsCredentialsNonExpired(true);
        verificationTokenRepo.delete(verificationToken);
        return new MessageResponse("Password reset successfully");
    }

    private String createRefreshToken(Credential credential){
        RefreshToken token = RefreshToken.builder()
                .token(randomToken())
                .expiresAt(Instant.now().plus(REFRESH_TTL))
                .revoked(false)
                .credential(credential)
                .build();
        return refreshTokenRepo.save(token).getToken();

    }

    //this token is to verify that the credentials belong to
    //the user attempting to acces it by email verification
    private String createVerificationToken(Credential credential, TokenPurpose purpose, Duration ttl){
        String value = randomToken();
        VerificationToken token =VerificationToken.builder()
                .token(value)
                .expireDate(LocalDateTime.now().plus(ttl))
                .credential(credential)
                .purpose(purpose)
                .build();
        return verificationTokenRepo.save(token).getToken();

    }

    private String randomToken(){
        byte[] bytes = new byte[48];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
