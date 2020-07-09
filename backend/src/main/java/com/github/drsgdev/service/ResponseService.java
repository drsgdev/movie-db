package com.github.drsgdev.service;

import java.util.List;
import java.util.Optional;
import com.github.drsgdev.dto.AuthResponse;
import com.github.drsgdev.dto.RefreshTokenRequest;
import com.github.drsgdev.dto.SignupRequest;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.security.AuthService;
import com.github.drsgdev.util.SignupFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseService {

  private final DBObjectService db;
  private final CreditsService credits;
  private final AuthService authService;

  private HttpStatus status;
  private String message;

  public static <T> ResponseEntity<T> createResponse(Optional<T> obj) {
    if (!obj.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.ok(obj.get());
  }

  public ResponseEntity<DBObject> fetchObjectById(Long id) {
    Optional<DBObject> object = db.findObjectById(id);

    return createResponse(object);
  }

  public ResponseEntity<List<DBObject>> fetchAllObjectsByType(String type) {
    Optional<List<DBObject>> objectList = db.findAllByType(type);

    return createResponse(objectList);
  }

  public ResponseEntity<List<DBObject>> fetchCreditsByPersonId(String id, String type) {
    Optional<List<DBObject>> castList = credits.findCreditsByPersonId(id, type);

    return createResponse(castList);
  }

  public ResponseEntity<List<DBObject>> fetchCreditsByMovieId(String id, String type) {
    Optional<List<DBObject>> castList = credits.findCreditsByMovieId(id, type);

    return createResponse(castList);
  }

  public ResponseEntity<String> signup(SignupRequest request) {
    status = HttpStatus.OK;
    message = "Signup complete";

    try {
      authService.signup(request);
    } catch (SignupFailedException ex) {
      log.info(ex.getMessage());

      status = HttpStatus.BAD_REQUEST;
      message = ex.getMessage();
    }

    return ResponseEntity.status(status).body(message);
  }

  public ResponseEntity<String> verifyUser(String token) {
    status = HttpStatus.OK;
    message = "User activated";

    try {
      authService.verify(token);
    } catch (SignupFailedException ex) {
      log.info(ex.getMessage());

      status = HttpStatus.BAD_REQUEST;
      message = ex.getMessage();
    }

    return ResponseEntity.status(status).body(message);
  }

  public ResponseEntity<AuthResponse> login(SignupRequest req) {
    status = HttpStatus.OK;
    message = "Login successful";


    String token = "";

    try {
      token = authService.login(req);
    } catch (SignupFailedException ex) {
      log.info(ex.getMessage());

      status = HttpStatus.BAD_REQUEST;
      message = ex.getMessage();
    }

    AuthResponse res = new AuthResponse();
    res.setUsername(req.getUsername());
    res.setToken(token);
    res.setMessage(message);

    if (status == HttpStatus.OK) {
      res.setExpiresAt(authService.tokenExpirationDate(token));
      res.setRefreshToken(authService.refreshToken(req.getUsername()));
    }

    return ResponseEntity.status(status).body(res);
  }

  public ResponseEntity<AuthResponse> refresh(RefreshTokenRequest req) {
    status = HttpStatus.OK;
    message = "Token refresh successful";

    String token = "";

    try {
      token = authService.refresh(req);
    } catch (SignupFailedException ex) {
      log.error(ex.getMessage());

      status = HttpStatus.BAD_REQUEST;
      message = ex.getMessage();
    }

    AuthResponse res = new AuthResponse();
    res.setUsername(req.getUsername());
    res.setToken(token);
    res.setMessage(message);

    if (status == HttpStatus.OK) {
      res.setExpiresAt(authService.tokenExpirationDate(token));
      res.setRefreshToken(authService.refreshToken(req.getUsername()));
    }

    return ResponseEntity.status(status).body(res);
  }

  public ResponseEntity<String> logout(RefreshTokenRequest req) {
    status = HttpStatus.OK;
    message = "User logged out";

    try {
      authService.logout(req);
    } catch (SignupFailedException ex) {
      log.error(ex.getMessage());

      status = HttpStatus.BAD_REQUEST;
      message = ex.getMessage();
    }

    return ResponseEntity.status(status).body(message);
  }
}
