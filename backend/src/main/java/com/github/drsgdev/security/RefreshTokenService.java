package com.github.drsgdev.security;

import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.service.DBObjectService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final DBObjectRepository objects;
    private final AttributeValueRepository attrValues;

    private final DBObjectService db;

    public String generateToken(String username) {
        String token = UUID.randomUUID().toString();

        DBObjectType type = db.findObjTypeByNameOrCreate("refresh_token");

        DBObject tokenObject = new DBObject();
        tokenObject.setType(type);
        tokenObject.setDescr(token);

        objects.save(tokenObject);

        db.saveOrUpdateNewAttributeValue(username, "text", "username", tokenObject);

        return token;
    }

    public boolean validateToken(String token, String username) {
        Optional<DBObject> tokenObject = findToken(username);
        if (!tokenObject.isPresent()) {
            return false;
        }

        if (!tokenObject.get().getDescr().equals(token)) {
            return false;
        }

        return true;
    }

    public boolean validateToken(String username) {
        return findToken(username).isPresent();
    }

    public void deleteToken(String token) {
        Optional<DBObject> tokenObject = objects.findByDescrAndTypeName(token, "refresh_token");

        delete(tokenObject.get());
    }

    public void deleteTokenByUsername(String username) {
        delete(findToken(username).get());
    }

    private void delete(DBObject tokenObject) {
        tokenObject.getAttributes().forEach((attr) -> {
            attrValues.delete(attr);
        });

        objects.delete(tokenObject);
    }

    private Optional<DBObject> findToken(String username) {
        return objects.findByTypeNameAndAttributesTypeNameAndAttributesVal("refresh_token",
                "username", username);
    }
}
