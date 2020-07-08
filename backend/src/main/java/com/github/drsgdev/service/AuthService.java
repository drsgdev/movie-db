package com.github.drsgdev.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import com.github.drsgdev.dto.SignupEmail;
import com.github.drsgdev.dto.SignupRequest;
import com.github.drsgdev.model.AttributeValue;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.AttributeRepository;
import com.github.drsgdev.repository.AttributeTypeRepository;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.repository.DBObjectTypeRepository;
import com.github.drsgdev.security.JWTProvider;
import com.github.drsgdev.util.SignupFailedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final DBObjectRepository objects;
  private final AttributeTypeRepository attrTypes;
  private final AttributeRepository attributes;
  private final AttributeValueRepository attrValues;
  private final DBObjectTypeRepository objTypes;

  private final PasswordEncoder encoder;
  private final EmailService email;
  private final AuthenticationManager auth;
  private final JWTProvider jwtProvider;

  public void signup(SignupRequest request) throws SignupFailedException {

    checkIfUserExists(request.getUsername(), request.getEmail());

    DBObject user = new DBObject();

    DBObjectType type = new DBObjectType();
    type.setName("user");

    user.setType(type);
    user.setDescr(request.getUsername());
    user.addAttribute("text", "username", request.getUsername());
    user.addAttribute("text", "password", encoder.encode(request.getPassword()));
    user.addAttribute("text", "email", request.getEmail());
    user.addAttribute("text", "created", Instant.now().toString());
    user.addAttribute("text", "enabled", Boolean.toString(false));

    String token = generateVerificationToken(user);


    objTypes.save(user.getType());
    objects.save(user);
    user.getAttributes().forEach((attr) -> {
      attrTypes.save(attr.getType().getType());
      attributes.save(attr.getType());
      attrValues.save(attr);
    });

    email.send(new SignupEmail(request.getEmail(), "Please activate your account",
        "http://localhost:8081/api/auth/verify?token=" + token));
  }

  public void verify(String token) throws SignupFailedException {
    Optional<DBObject> user = objects.findByTypeNameAndAttributesTypeNameAndAttributesVal("user",
        "verification_token", token);

    if (!user.isPresent()) {
      throw new SignupFailedException("User with the specified token was not found");
    }

    Optional<AttributeValue> isEnabled =
        attrValues.findByTypeNameAndObjectId("enabled", user.get().getId());

    if (isEnabled.get().getVal().equals("true")) {
      throw new SignupFailedException("User is already activated");
    } else {
      isEnabled.get().setVal("true");
      attrValues.save(isEnabled.get());
    }
  }

  public String login(SignupRequest request) throws SignupFailedException {

    Authentication authentication;
    try {
      log.info("Trying to log user {} with password {}", request.getUsername(), request.getPassword());
      authentication = auth.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    } catch (AuthenticationException ex) {
      throw new SignupFailedException("Authentication failed: " + ex.getMessage());
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = jwtProvider.generateJWTToken(authentication);

    return token;
  }

  private void checkIfUserExists(String username, String email) throws SignupFailedException {
    objects.findByDescrAndTypeName(username, "user").ifPresent((obj) -> {
      throw new SignupFailedException("Username already exists");
    });

    objects.findByTypeNameAndAttributesTypeNameAndAttributesVal("user", "email", email)
        .ifPresent((obj) -> {
          throw new SignupFailedException("Email already exists");
        });
  }

  private String generateVerificationToken(DBObject user) {
    String token = UUID.randomUUID().toString();

    user.addAttribute("text", "verification_token", token);

    return token;
  }
}
