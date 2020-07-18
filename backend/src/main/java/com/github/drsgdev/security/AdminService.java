package com.github.drsgdev.security;

import javax.annotation.PostConstruct;
import com.github.drsgdev.dto.SignupRequest;
import com.github.drsgdev.util.SignupFailedException;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final AuthService auth;

    @PostConstruct
    private void createAdmin() {
        SignupRequest req = new SignupRequest();
        req.setUsername("admin");
        req.setPassword("admin");
        req.setEmail("admin@movie-db.org");

        try {
            auth.signup(req);
        } catch (SignupFailedException ex) {
            log.info("Admin already exists");
        }
    }
}
