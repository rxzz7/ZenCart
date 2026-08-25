package com.zencart.user_service.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtilImpl implements JwtUtil{


    // secret key is getting injected from application properties
//    @Value("${jwt.secret}")
//    private String secret;


    //generating the key(once) || its is encoded Base64  and stored in jwt.secret
//    public JwtUtilImpl() throws NoSuchAlgorithmException {
//    }
//
//    KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//    SecretKey sKey = keyGenerator.generateKey();
//    String secretKey = Base64.getEncoder().encodeToString(sKey.getEncoded());
//    System.out.println("Secret Key: " + secretKey);
//


        //Generate JWT RSA keys
//        mkdir keys
//
//    openssl genrsa -out keys/private_key.pem 2048
//
//    openssl rsa \
//            -in keys/private_key.pem \
//            -pubout \
//            -out keys/public_key.pem

    //The key is originally in PKCS#8 format, it is encoded in Base64 format and stored in .pem file
    @Value("${jwt.private-key-location:classpath:keys/private_key.pem}")
    Resource privateKeyResource;

    @Getter
    private final long expiration = 1000 * 60 * 60;

    //converting the secret key representation from Base64 text back into bytes.
//    private SecretKey getSigningKey(){
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
//    }

    private PrivateKey getSigningKey(){
        try {
            String pem = new String(privateKeyResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String base64 = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(base64);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        }catch (Exception ex){
            throw new IllegalStateException("Unable to load JWT RSA private key");
        }
    }

    @Override
    public String generateToken(String subject, String role) {
        Date now = new Date();
        return Jwts.builder()
                .subject(subject)
                .claim("roles",java.util.List.of(role))
                .issuer("zencart-auth-service")
                .audience().add("zencart-api").and()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(getSigningKey(),Jwts.SIG.RS256) //algorithm RSA + SHA256
                .compact();
    }

/// /////////
//    @Override
//    public String extractUsername(String token) {
//        return Jwts.parser()
//                .verifyWith(getSigningKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject();
//    }
//
//    @Override
//    public boolean validateToken(String token, String username) {
//        try {
//            return username.equals(extractUsername(token));
//        } catch (JwtException | IllegalArgumentException ex) {
//            return false;
//        }
//    }
}
