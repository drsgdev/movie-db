package com.github.drsgdev.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.github.drsgdev.dto.FavoriteRequest;
import com.github.drsgdev.model.AttributeValue;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.util.UserException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final DBObjectRepository objects;
    private final AttributeValueRepository attributes;

    private final DBObjectService db;

    public void addFavorite(FavoriteRequest req) {
        DBObject user = findUser(req.getUsername()).get();
        Optional<AttributeValue> favorites = findFavorites(user.getId());

        if (!favorites.isPresent()) {
            db.saveOrUpdateNewAttributeValue(req.getId(), "text", "favorites", user);
        } else {
            List<String> ids = new ArrayList<>(Arrays.asList(favorites.get().getVal().split(",")));
            if (!ids.contains(req.getId())) {
                ids.add(req.getId());

                StringBuilder newFavorites = new StringBuilder();
                ids.parallelStream().forEach(id -> {
                    newFavorites.append(id + ",");
                });

                favorites.get().setVal(newFavorites.toString());
                attributes.save(favorites.get());
            }
        }
    }

    public List<DBObject> getFavorites(String username) {
        DBObject user = findUser(username).get();
        Optional<AttributeValue> favorites = findFavorites(user.getId());;

        if (!favorites.isPresent()) {
            throw new UserException("User " + username + " has no favorites");
        }

        List<String> ids = new ArrayList<>(Arrays.asList(favorites.get().getVal().split(",")));
        List<DBObject> res = ids.parallelStream().map(id -> {
            Optional<DBObject> title = db.findObjectById(Long.parseLong(id));

            if (title.isPresent()) {
                return title.get();
            } else {
                return new DBObject();
            }
        }).collect(Collectors.toList());

        db.mapAttributes(res);

        return res;
    }

    public List<DBObject> getRated(String username) {
        Optional<List<DBObject>> rated =
                objects.findAllByTypeNameAndAttributesTypeNameAndAttributesVal("rating", "username",
                        username);

        if (!rated.isPresent()) {
            throw new UserException("User " + username + " has no rated titles");
        }

        db.mapAttributes(rated.get());

        return mapTitles(rated.get());
    }

    public List<DBObject> getReviewed(String username) {
        Optional<List<DBObject>> reviewed =
                objects.findAllByTypeNameAndAttributesTypeNameAndAttributesVal("review", "username",
                        username);

        if (!reviewed.isPresent()) {
            throw new UserException("User " + username + " has no rated titles");
        }

        db.mapAttributes(reviewed.get());


        return mapTitles(reviewed.get());
    }

    public Optional<DBObject> findUser(String username) {
        return objects.findByDescrAndTypeName(username, "user");
    }

    public Optional<AttributeValue> findFavorites(Long userId) {
        return attributes.findByTypeNameAndObjectId("favorites", userId);
    }

    public List<DBObject> mapTitles(List<DBObject> mapFrom) {
        List<DBObject> titles = mapFrom.parallelStream().map(rating -> {

            return objects.findById(Long.parseLong(rating.getAttributeMap().get("id"))).get();

        }).collect(Collectors.toList());

        db.mapAttributes(titles);
        return titles;
    }
}
