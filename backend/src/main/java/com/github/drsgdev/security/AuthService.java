package com.github.drsgdev.security;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import com.github.drsgdev.dto.RefreshTokenRequest;
import com.github.drsgdev.dto.SignupEmail;
import com.github.drsgdev.dto.SignupRequest;
import com.github.drsgdev.model.AttributeValue;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.repository.DBObjectTypeRepository;
import com.github.drsgdev.service.DBObjectService;
import com.github.drsgdev.service.EmailService;
import com.github.drsgdev.util.AttrTypes;
import com.github.drsgdev.util.SignupFailedException;
import com.github.drsgdev.util.Types;
import com.github.drsgdev.util.UserException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final DBObjectRepository objects;
    private final AttributeValueRepository attrValues;
    private final DBObjectTypeRepository objTypes;

    private final PasswordEncoder encoder;
    private final EmailService email;
    private final AuthenticationManager auth;
    private final JWTProvider jwtProvider;
    private final UserDetailsServiceImpl userDetails;

    private final RefreshTokenService refreshService;
    private final DBObjectService db;

    public void signup(SignupRequest request) throws SignupFailedException {

        if (request.getUsername().isEmpty() || request.getEmail().isEmpty()
                || request.getPassword().isEmpty()) {
            throw new SignupFailedException("Signup failed: Bad credentials");
        }

        log.info("New user: {} {}", request.getUsername(), request.getEmail());

        checkIfUserExists(request.getUsername(), request.getEmail());

        DBObject user = new DBObject();

        DBObjectType type = new DBObjectType();
        type.setName("user");

        String role = request.getUsername().equals("admin") ? "admin" : "user";
        String enabled = request.getUsername().equals("admin") ? "true" : "false";

        user.setType(type);
        user.setDescr(request.getUsername());
        user.addAttribute(AttrTypes.TEXT, "username", request.getUsername());
        user.addAttribute(AttrTypes.TEXT, "password", encoder.encode(request.getPassword()));
        user.addAttribute(AttrTypes.TEXT, "email", request.getEmail());
        user.addAttribute(AttrTypes.TEXT, "created", Instant.now().toString());
        user.addAttribute(AttrTypes.TEXT, "enabled", enabled);
        user.addAttribute(AttrTypes.TEXT, "locked", Boolean.toString(false));
        user.addAttribute(AttrTypes.TEXT, "role", role);

        String token = generateVerificationToken(user);

        objTypes.save(user.getType());
        objects.save(user);
        user.getAttributes().forEach((attr) -> {
            db.saveOrUpdateNewAttributeValue(attr.getVal(),
                    AttrTypes.parseValue(attr.getType().getType().getName()),
                    attr.getType().getName(), user);
        });

        log.info("Saved new user: {}", request.getUsername());

        if (request.getUsername() != "admin") {
            email.send(new SignupEmail(request.getEmail(), "Please activate your account",
                    "http://localhost:8081/api/auth/verify?token=" + token));
        }
    }

    public void verify(String token) throws SignupFailedException {
        Optional<DBObject> user = objects.findByTypeNameAndAttributesTypeNameAndAttributesVal(
                "user", "verification_token", token);

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
            log.info("Trying to log user {}", request.getUsername());
            authentication =
                    auth.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword()));

        } catch (AuthenticationException ex) {
            throw new SignupFailedException("Authentication failed: " + ex.getMessage());
        }

        if (refreshService.validateToken(request.getUsername())) {
            refreshService.deleteTokenByUsername(request.getUsername());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateJWT(authentication);

        return token;
    }

    public void status(String username) throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new UserException("User is not authenticated");
        }

        UserDetails user = userDetails.loadUserByUsername(username);
        if (!user.isEnabled()) {
            throw new UserException("User is disabled");
        }
        if (!user.isAccountNonLocked()) {
            throw new UserException("User is banned");
        }
        if (!refreshService.validateToken(username)) {
            throw new UserException("User is logged out");
        }
    }

    public String refresh(RefreshTokenRequest request) throws SignupFailedException {
        if (!refreshService.validateToken(request.getToken(), request.getUsername())) {
            throw new SignupFailedException("Refresh token is invalid");
        }
        refreshService.deleteToken(request.getToken());

        String newToken = jwtProvider.generateJWTUsername(request.getUsername());

        return newToken;
    }

    public void logout(RefreshTokenRequest request) throws SignupFailedException {

        boolean exists = false;

        try {
            checkIfUsernameExists(request.getUsername());
        } catch (SignupFailedException ex) {
            log.info("Loggin out user {}", request.getUsername());
            exists = true;
        }

        if (!refreshService.validateToken(request.getToken(), request.getUsername()) || !exists) {
            throw new SignupFailedException("Refresh token is invalid");
        }

        refreshService.deleteToken(request.getToken());
        SecurityContextHolder.clearContext();
    }

    private void checkIfUserExists(String username, String email) throws SignupFailedException {

        checkIfUsernameExists(username);

        objects.findByTypeNameAndAttributesTypeNameAndAttributesVal("user", "email", email)
                .ifPresent((obj) -> {
                    throw new SignupFailedException("Email already exists");
                });
    }

    private void checkIfUsernameExists(String username) throws SignupFailedException {
        objects.findByDescrAndTypeName(username, "user").ifPresent((obj) -> {
            throw new SignupFailedException("Username already exists");
        });
    }

    public Date tokenExpirationDate(String token) {
        return jwtProvider.getExpirationFromJWT(token);
    }

    public String refreshToken(String username) {
        return refreshService.generateToken(username);
    }

    private String generateVerificationToken(DBObject user) {
        String token = UUID.randomUUID().toString();

        user.addAttribute(AttrTypes.TEXT, "verification_token", token);

        return token;
    }

    public void ban(String username) {
        setLocked(username, true);
    }

    public void unban(String username) {
        setLocked(username, false);
    }

    private void setLocked(String username, Boolean value) {
        DBObject user = objects.findByDescrAndTypeName(username, Types.USER.getValue()).get();
        db.saveOrUpdateNewAttributeValue(value.toString(), AttrTypes.TEXT, "locked", user);
    }
}
