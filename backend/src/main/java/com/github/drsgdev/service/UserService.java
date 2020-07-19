package com.github.drsgdev.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.github.drsgdev.dto.FavoriteRequest;
import com.github.drsgdev.dto.LastVisitedRequest;
import com.github.drsgdev.model.AttributeValue;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.util.AttrTypes;
import com.github.drsgdev.util.Types;
import com.github.drsgdev.util.UserException;
import com.github.drsgdev.util.UserListRequest;
import com.github.drsgdev.util.UserListTypes;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final DBObjectRepository objects;
    private final AttributeValueRepository attributes;

    private final DBObjectService db;

    public void addFavorite(FavoriteRequest req) {
        addTitleToList(req, UserListTypes.FAVORITES);
    }

    public void removeFavorite(FavoriteRequest req) {
        removeTitleFromList(req, UserListTypes.FAVORITES);
    }

    public void addLastVisited(LastVisitedRequest req) {
        addTitleToList(req, UserListTypes.LAST_VISITED);
    }

    private void addTitleToList(UserListRequest req, UserListTypes listType) {
        DBObject user = findUser(req.getUsername()).get();
        Optional<AttributeValue> userList = findUserList(user.getId(), listType);

        if (!userList.isPresent()) {
            db.saveOrUpdateNewAttributeValue(req.getId(), AttrTypes.TEXT, listType.getValue(),
                    user);

            log.info("Saved new list {} for user {}", listType.getValue(), req.getUsername());
        } else {
            List<String> ids = new ArrayList<>(Arrays.asList(userList.get().getVal().split(",")));
            if (!ids.contains(req.getId())) {
                if (ids.size() == 10) {
                    ids.remove(ids.size() - 1);
                }
                ids.add(req.getId());

                StringBuilder newFavorites = new StringBuilder();
                ids.parallelStream().forEach(id -> {
                    newFavorites.append(id + ",");
                });

                userList.get().setVal(newFavorites.toString());
                attributes.save(userList.get());
            }
        }
    }

    private void removeTitleFromList(UserListRequest req, UserListTypes listType) {
        DBObject user = findUser(req.getUsername()).get();
        Optional<AttributeValue> userList = findUserList(user.getId(), listType);

        if (userList.isPresent()) {
            List<String> ids = new ArrayList<>(Arrays.asList(userList.get().getVal().split(",")));
            if (ids.contains(req.getId())) {
                ids.remove(req.getId());

                StringBuilder newFavorites = new StringBuilder();
                ids.parallelStream().forEach(id -> {
                    newFavorites.append(id + ",");
                });

                userList.get().setVal(newFavorites.toString());
                attributes.save(userList.get());
            }

            log.info("Removed id {} from {}'s list {}", req.getId(), req.getUsername(),
                    listType.getValue());
        }
    }

    public List<DBObject> getFavorites(String username) {
        return getList(username, UserListTypes.FAVORITES);
    }

    public List<DBObject> getLastVisited(String username) {
        return getList(username, UserListTypes.LAST_VISITED);
    }

    private List<DBObject> getList(String username, UserListTypes listType) {
        Optional<DBObject> user = findUser(username);

        if (!user.isPresent()) {
            throw new UserException("User " + username + " not found");
        }

        Optional<AttributeValue> favorites = findUserList(user.get().getId(), listType);

        if (!favorites.isPresent()) {
            throw new UserException("User " + username + " has no " + listType.getValue());
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
        return objects.findByDescrAndTypeName(username, Types.USER.getValue());
    }

    public Optional<List<DBObject>> findAllUsers() {
        return objects.findAllByTypeName(Types.USER.getValue());
    }

    public Optional<AttributeValue> findUserList(Long userId, UserListTypes listType) {
        return attributes.findByTypeNameAndObjectId(listType.getValue(), userId);
    }

    public List<DBObject> mapTitles(List<DBObject> mapFrom) {
        List<DBObject> titles = mapFrom.parallelStream().map(rating -> {

            return objects.findById(Long.parseLong(rating.getAttributeMap().get("id"))).get();

        }).collect(Collectors.toList());

        db.mapAttributes(titles);
        return titles;
    }

    public boolean hasInFavorites(FavoriteRequest req) {
        DBObject user = findUser(req.getUsername()).get();
        Optional<AttributeValue> favorites = findUserList(user.getId(), UserListTypes.FAVORITES);

        if (!favorites.isPresent()) {
            return false;
        }

        return favorites.get().getVal().contains(req.getId());
    }
}
