package com.github.drsgdev.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;
import javax.annotation.PostConstruct;
import com.github.drsgdev.util.SignupFailedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTProvider {

    private KeyStore store;

    @Getter
    private Long expireTime = 600000l; // 10m jwt expiration time

    @PostConstruct
    public void init() {
        try {
            store = KeyStore.getInstance("JKS");

            InputStream keyStoreStream = getClass().getResourceAsStream("/keystore.jks");
            store.load(keyStoreStream, "passwordkeytool".toCharArray());

            log.info("Successfuly loaded key store {}", store.getType());

        } catch (CertificateException | IOException | KeyStoreException
                | NoSuchAlgorithmException ex) {
            throw new SignupFailedException(
                    "Error occurred while loading key store: " + ex.getMessage());
        }
    }

    public String generateJWT(Authentication auth) {
        UserDetails principal = (UserDetails) auth.getPrincipal();

        String token = generateJWTUsername(principal.getUsername());

        return token;
    }

    public String generateJWTUsername(String username) {
        String token = Jwts.builder().setSubject(username).setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(expireTime)))
                .signWith(getPrivateKey()).compact();

        log.info("Generated new token {} for {} expires at {}", token, username,
                Date.from(Instant.now().plusMillis(expireTime)));
        return token;
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) store.getKey("keystore", "passwordkeytool".toCharArray());

        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            throw new SignupFailedException("Error occured while loading private key");
        }
    }

    private PublicKey getPublicKey() {
        try {
            return store.getCertificate("keystore").getPublicKey();

        } catch (KeyStoreException ex) {
            throw new SignupFailedException("Error occured while loading public key");
        }
    }

    public boolean validateJWT(String jwt) {
        parseClaims(jwt);

        return true;
    }

    public String getUsernameFromJWT(String jwt) {
        Claims claims = parseClaims(jwt).getBody();

        return claims.getSubject();
    }

    public Date getExpirationFromJWT(String jwt) {
        Claims claims = parseClaims(jwt).getBody();

        return claims.getExpiration();
    }

    private Jws<Claims> parseClaims(String jwt) {
        return Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
    }
}
