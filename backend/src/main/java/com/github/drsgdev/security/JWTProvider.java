package com.github.drsgdev.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.annotation.PostConstruct;
import com.github.drsgdev.util.SignupFailedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWTProvider {

  private KeyStore store;

  @PostConstruct
  public void init() {
    try {
      store = KeyStore.getInstance("JKS");

      InputStream keyStoreStream = getClass().getResourceAsStream("/keystore.jks");
      store.load(keyStoreStream, "passwordkeytool".toCharArray());

      log.info("Successfuly loaded key store {}", store.getType());

    } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException ex) {
      throw new SignupFailedException("Error occurred while loading key store: " + ex.getMessage());
    }
  }

  public String generateJWTToken(Authentication auth) {
    UserDetails principal = (UserDetails) auth.getPrincipal();

    return Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivateKey()).compact();
  }

  private PrivateKey getPrivateKey() {
    try {
      return (PrivateKey) store.getKey("keystore", "passwordkeytool".toCharArray());

    } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
      throw new SignupFailedException("Error occured while loading private key");
    }
  }
}
