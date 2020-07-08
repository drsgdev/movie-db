package com.github.drsgdev.security;

import java.util.UUID;
import javax.transaction.Transactional;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.repository.DBObjectTypeRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final DBObjectRepository objects;
    private final DBObjectTypeRepository types;

    public String generateToken() {
        String token = UUID.randomUUID().toString();

        DBObjectType type = new DBObjectType();
        type.setName("refresh_token");

        types.save(type);

        DBObject tokenObject = new DBObject();
        tokenObject.setType(type);
        tokenObject.setDescr(token);

        objects.save(tokenObject);

        return token;
    }

    public boolean validateToken(String token) {
        return objects.findByDescrAndTypeName(token, "refresh_token").isPresent();
    }

    public void deleteToken(String token) {
        objects.deleteByDescrAndTypeName(token, "refresh_token");
    }
}
