package com.github.drsgdev.service;

import java.util.Optional;
import com.github.drsgdev.model.AttributeValue;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.util.RatingException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final DBObjectRepository objects;
    private final AttributeValueRepository attributes;

    private final DBObjectService db;

    public void rate(Long id, Integer rate) {
        Optional<DBObject> objFromDB = objects.findById(id);

        DBObject rating = db.findObjOrCreate("rating", objFromDB.get().getDescr());
        String parsedRate = "";

        switch (rate) {
            case 1:
                parsedRate = "very_bad";
                break;
            case 2:
                parsedRate = "bad";
                break;
            case 3:
                parsedRate = "ok";
                break;
            case 4:
                parsedRate = "good";
                break;
            case 5:
                parsedRate = "very_good";
                break;
        }

        Optional<AttributeValue> rateFromDB = attributes.findByTypeNameAndObjectId(parsedRate, id);
        if (rateFromDB.isPresent()) {
            Integer rateValue = Integer.parseInt(rateFromDB.get().getVal());
            rateValue += rate;

            rateFromDB.get().setVal(rateValue.toString());
            attributes.save(rateFromDB.get());
        } else {
            db.saveOrUpdateNewAttributeValue(rate.toString(), "text", parsedRate, rating);
        }
    }

    public DBObject getRatingById(Long id) throws RatingException {
        Optional<DBObject> rating = objects
                .findByTypeNameAndAttributesTypeNameAndAttributesVal("rating", "id", id.toString());

        if (!rating.isPresent()) {
            throw new RatingException("Rating for not found");
        }

        return rating.get();
    }
}
