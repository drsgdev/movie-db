package com.github.drsgdev.service;

import java.util.List;
import java.util.Optional;
import com.github.drsgdev.dto.AuthResponse;
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

  private final DBObjectService objectService;
  private final CreditsService personService;
  private final AuthService authService;

  public static <T> ResponseEntity<T> createResponse(Optional<T> obj) {
    if (!obj.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.ok(obj.get());
  }

  public ResponseEntity<DBObject> fetchObjectById(Long id) {
    Optional<DBObject> object = objectService.findObjectById(id);

    return createResponse(object);
  }

  public ResponseEntity<List<DBObject>> fetchAllObjectsByType(String type) {
    Optional<List<DBObject>> objectList = objectService.findAllByType(type);

    return createResponse(objectList);
  }

  public ResponseEntity<List<DBObject>> fetchCreditsByPersonId(String id, String type) {
    Optional<List<DBObject>> castList = personService.findCreditsByPersonId(id, type);

    return createResponse(castList);
  }

  public ResponseEntity<List<DBObject>> fetchCreditsByMovieId(String id, String type) {
    Optional<List<DBObject>> castList = personService.findCreditsByMovieId(id, type);

    return createResponse(castList);
  }

  public ResponseEntity<String> addUserToDB(SignupRequest request) {
    HttpStatus status = HttpStatus.OK;
    String response = "Signup complete";

    try {
      authService.signup(request);
    } catch (SignupFailedException ex) {
      log.info(ex.getMessage());

      status = HttpStatus.BAD_REQUEST;
      response = ex.getMessage();
    }

    return ResponseEntity.status(status).body(response);
  }

  public ResponseEntity<String> verifyUser(String token) {
    HttpStatus status = HttpStatus.OK;
    String response = "User activated";

    try {
      authService.verify(token);
    } catch (SignupFailedException ex) {
      log.info(ex.getMessage());

      status = HttpStatus.BAD_REQUEST;
      response = ex.getMessage();
    }

    return ResponseEntity.status(status).body(response);
  }

  public ResponseEntity<AuthResponse> login(SignupRequest req) {
    HttpStatus status = HttpStatus.OK;
    String token = "";
    String message = "Login successful";

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

    return ResponseEntity.status(status).body(res);
  }
}
