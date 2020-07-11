package com.github.drsgdev.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.util.RatingException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {

    public enum Rate {
        VERY_BAD(1), BAD(2), OK(3), GOOD(4), VERY_GOOD(5);

        public int value;

        private Rate(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    };

    private final DBObjectRepository objects;
    private final AttributeValueRepository attributes;

    private final DBObjectService db;

    public void rate(Long id, Integer rate, String username) {
        Optional<DBObject> objFromDB = objects.findById(id);

        DBObject rating = db.findObjOrCreate("rating", objFromDB.get().getDescr() + username);

        db.saveOrUpdateNewAttributeValue(id.toString(), "text", "id", rating);
        db.saveOrUpdateNewAttributeValue(username, "text", "username", rating);
        db.saveOrUpdateNewAttributeValue(rate.toString(), "text", "rate", rating);
    }

    public List<Integer> getRatingById(Long id) throws RatingException {
        Optional<List<DBObject>> ratings =
                objects.findAllByTypeNameAndAttributesTypeNameAndAttributesVal("rating", "id",
                        id.toString());

        if (!ratings.isPresent()) {
            throw new RatingException("Rating not found");
        }

        ratings.get().parallelStream().forEach((rating) -> db.mapAttributes(rating));

        List<Integer> rates = Arrays.asList(0, 0, 0, 0, 0);
        ratings.get().parallelStream().forEach((rate) -> {
            Integer rateValue = Integer.parseInt(rate.getAttributeMap().get("rate"));

            rates.set(rateValue - 1, rates.get(rateValue - 1) + 1);
        });

        Collections.reverse(rates);
        return rates;
    }
}
