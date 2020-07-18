package com.github.drsgdev.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.github.drsgdev.dto.AuthResponse;
import com.github.drsgdev.dto.FavoriteRequest;
import com.github.drsgdev.dto.LastVisitedRequest;
import com.github.drsgdev.dto.RatingRequest;
import com.github.drsgdev.dto.RefreshTokenRequest;
import com.github.drsgdev.dto.ReviewRequest;
import com.github.drsgdev.dto.SignupRequest;
import com.github.drsgdev.dto.UserProfileResponse;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.security.AuthService;
import com.github.drsgdev.util.RatingException;
import com.github.drsgdev.util.RenewAction;
import com.github.drsgdev.util.SignupFailedException;
import com.github.drsgdev.util.Types;
import com.github.drsgdev.util.UserException;
import com.github.drsgdev.util.UserListTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final PersonService people;
    private final AuthService authService;
    private final RatingService ratingService;
    private final UserService userService;

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

    public ResponseEntity<List<DBObject>> fetchAllObjectsByType(Types type) {
        Optional<List<DBObject>> objectList = db.findAllByType(type);

        return createResponse(objectList);
    }

    public ResponseEntity<List<DBObject>> fetchCreditsByPersonId(String id, Types type) {
        Optional<List<DBObject>> castList = credits.findCreditsByPersonId(id, type);

        return createResponse(castList);
    }

    public ResponseEntity<List<DBObject>> fetchCreditsByTitleId(String id, Types type) {
        Optional<List<DBObject>> castList = credits.findCreditsByTitleId(id, type);

        return createResponse(castList);
    }

    public ResponseEntity<DBObject> fetchPersonById(Long id) {
        Optional<DBObject> person = people.findPersonByUserId(id);

        return createResponse(person);
    }

    public ResponseEntity<UserProfileResponse> fetchUserProfile(String username) {
        Optional<DBObject> user = userService.findUser(username);
        HttpStatus status = HttpStatus.OK;
        UserProfileResponse res = new UserProfileResponse();

        if (user.isPresent()) {
            db.mapAttributes(user.get());

            res.setUsername(username);
            res.setEmail(user.get().getAttributeMap().get("email"));
            res.setCreated(user.get().getAttributeMap().get("created"));
            res.setLocked(user.get().getAttributeMap().get("locked"));
            res.setRole(user.get().getAttributeMap().get("role"));
        } else {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(res);
    }

    public ResponseEntity<List<UserProfileResponse>> fetchAllUsers() {
        Optional<List<DBObject>> users = userService.findAllUsers();
        HttpStatus status = HttpStatus.OK;
        List<UserProfileResponse> res = new ArrayList<>();

        if (users.isPresent()) {
            db.mapAttributes(users.get());

            res = users.get().parallelStream().map(user -> {
                UserProfileResponse profile = new UserProfileResponse();
                profile.setUsername(user.getDescr());
                profile.setEmail(user.getAttributeMap().get("email"));
                profile.setCreated(user.getAttributeMap().get("created"));
                profile.setLocked(user.getAttributeMap().get("locked"));
                profile.setRole(user.getAttributeMap().get("role"));

                return profile;
            }).collect(Collectors.toList());
        } else {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(res);
    }

    public ResponseEntity<String> signup(SignupRequest request) {
        HttpStatus status = HttpStatus.OK;
        String message = "Signup complete";

        try {
            authService.signup(request);
        } catch (SignupFailedException ex) {
            log.warn(ex.getMessage());

            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        }

        return ResponseEntity.status(status).body(message);
    }

    public ResponseEntity<String> verifyUser(String token) {
        HttpStatus status = HttpStatus.OK;
        String message = "User activated";

        try {
            authService.verify(token);
        } catch (SignupFailedException ex) {
            log.warn(ex.getMessage());

            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        }

        return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(message);
    }

    public ResponseEntity<AuthResponse> login(SignupRequest req) {
        return renew(req, RenewAction.LOGIN);
    }

    public ResponseEntity<AuthResponse> refresh(RefreshTokenRequest req) {
        return renew(req, RenewAction.REFRESH);
    }

    private <T> ResponseEntity<AuthResponse> renew(T req, RenewAction action) {
        HttpStatus status = HttpStatus.OK;
        String message = "Token refresh successful";

        String token = "";
        String username = "";

        try {
            switch (action) {
                case LOGIN:
                    token = authService.login((SignupRequest) req);
                    username = ((SignupRequest) req).getUsername();
                    break;
                case REFRESH:
                    token = authService.refresh((RefreshTokenRequest) req);
                    username = ((RefreshTokenRequest) req).getUsername();
                default:
                    break;
            }
        } catch (SignupFailedException ex) {
            log.warn(ex.getMessage());

            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        }

        AuthResponse res = new AuthResponse();
        res.setUsername(username);
        res.setToken(token);
        res.setMessage(message);

        if (status != HttpStatus.BAD_REQUEST) {
            res.setExpiresAt(authService.tokenExpirationDate(token).getTime());
            res.setRefreshToken(authService.refreshToken(username));
        }

        return ResponseEntity.status(status).body(res);
    }

    public ResponseEntity<String> logout(RefreshTokenRequest req) {
        HttpStatus status = HttpStatus.OK;
        String message = "User logged out";

        try {
            authService.logout(req);
        } catch (SignupFailedException ex) {
            log.warn(ex.getMessage());

            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        }

        return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(message);
    }

    public ResponseEntity<String> status(String username) {
        HttpStatus status = HttpStatus.OK;
        String message = "User is logged in";

        try {
            authService.status(username);
        } catch (UserException ex) {
            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        }

        return ResponseEntity.status(status).body(message);
    }

    public ResponseEntity<String> ban(String username) {
        if (username.equals("admin")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin cannot be banned");
        }
        String message = "User banned";

        authService.ban(username);

        return ResponseEntity.ok(message);
    }

    public ResponseEntity<String> unban(String username) {
        String message = "User unbanned";

        authService.unban(username);

        return ResponseEntity.ok(message);
    }

    public ResponseEntity<String> rateObject(RatingRequest req) {
        HttpStatus status = HttpStatus.OK;
        String message = "Rating saved";

        ratingService.rate(req);

        return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(message);
    }

    public ResponseEntity<List<Integer>> getRatingById(Long id) {
        HttpStatus status = HttpStatus.OK;

        List<Integer> rating = new ArrayList<>();

        try {
            rating = ratingService.getRatingById(id);
        } catch (RatingException ex) {
            log.warn("{} for id {}", ex.getMessage(), id);

            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(rating);
    }

    public ResponseEntity<String> reviewObject(ReviewRequest req) {
        HttpStatus status = HttpStatus.OK;
        String message = "Review saved";

        ratingService.review(req);

        return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(message);
    }

    public ResponseEntity<List<ReviewRequest>> getReviewsById(Long id) {
        HttpStatus status = HttpStatus.OK;

        List<ReviewRequest> reviews = new ArrayList<>();

        try {
            reviews = ratingService.getReviewsById(id);
        } catch (RatingException ex) {
            log.warn("{} for id {}", ex.getMessage(), id);

            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(reviews);
    }

    public ResponseEntity<String> addTitleToFavorites(FavoriteRequest req) {
        HttpStatus status = HttpStatus.OK;
        String message = "Title was added to your favorites";

        userService.addFavorite(req);

        return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(message);
    }

    public ResponseEntity<String> removeTitleFromFavorites(FavoriteRequest req) {
        HttpStatus status = HttpStatus.OK;
        String message = "Title was removed from your favorites";

        userService.removeFavorite(req);

        return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(message);
    }

    public void addTitleToLastVisited(LastVisitedRequest req) {
        userService.addLastVisited(req);
    }

    public ResponseEntity<List<DBObject>> getUserLastVisited(String username) {
        return getUserTitles(username, UserListTypes.LAST_VISITED);
    }

    public ResponseEntity<List<DBObject>> getUserFavorites(String username) {
        return getUserTitles(username, UserListTypes.FAVORITES);
    }

    public ResponseEntity<List<DBObject>> getUserRated(String username) {
        return getUserTitles(username, UserListTypes.RATED);
    }

    public ResponseEntity<List<DBObject>> getUserReviewed(String username) {
        return getUserTitles(username, UserListTypes.REVIEWED);
    }

    private ResponseEntity<List<DBObject>> getUserTitles(String username, UserListTypes type) {
        HttpStatus status = HttpStatus.OK;

        List<DBObject> res = new ArrayList<>();

        try {
            switch (type) {
                case FAVORITES:
                    res = userService.getFavorites(username);
                    break;
                case RATED:
                    res = userService.getRated(username);
                    break;
                case REVIEWED:
                    res = userService.getReviewed(username);
                    break;
                case LAST_VISITED:
                    res = userService.getLastVisited(username);
                    break;
            }
        } catch (UserException ex) {
            log.warn(ex.getMessage());

            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(res);
    }

    public ResponseEntity<Boolean> isInFavorites(FavoriteRequest req) {
        HttpStatus status = HttpStatus.OK;

        boolean isFavorite = userService.hasInFavorites(req);
        if (!isFavorite) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(isFavorite);
    }
}
