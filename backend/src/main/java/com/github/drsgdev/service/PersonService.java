package com.github.drsgdev.service;

import java.util.Optional;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.DBObjectRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final ApiService api;
    private final DBObjectService db;

    private final DBObjectRepository objects;

    public Optional<DBObject> findPersonByUserId(Long id) {
        Optional<DBObject> personFromDB = objects
                .findByTypeNameAndAttributesTypeNameAndAttributesVal("person", "id", id.toString());

        if (!personFromDB.isPresent()) {
            api.addPersonToDB(id.toString());

            personFromDB = objects.findByTypeNameAndAttributesTypeNameAndAttributesVal("person",
                    "id", id.toString());
            if (!personFromDB.isPresent()) {
                return Optional.empty();
            }
        }

        log.info("Found person {} id {}", personFromDB.get().getDescr(), id.toString());

        db.mapAttributes(personFromDB.get());

        return personFromDB;
    }
}
