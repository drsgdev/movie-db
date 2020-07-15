package com.github.drsgdev.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.github.drsgdev.dto.RatingRequest;
import com.github.drsgdev.dto.ReviewRequest;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.util.RatingException;
import com.github.drsgdev.util.Types;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    public enum Rate {
        VERY_BAD(1), BAD(2), OK(3), GOOD(4), VERY_GOOD(5);

        public int value;

        Rate(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    };

    private final DBObjectRepository objects;

    private final DBObjectService db;

    public void rate(RatingRequest req) {
        Optional<DBObject> objFromDB = objects.findById(req.getId());

        DBObject rating =
                db.findObjOrCreate(Types.RATING, objFromDB.get().getDescr() + req.getUsername());

        db.saveOrUpdateNewAttributeValue(req.getId().toString(), "text", "id", rating);
        db.saveOrUpdateNewAttributeValue(req.getUsername(), "text", "username", rating);
        db.saveOrUpdateNewAttributeValue(req.getRate().toString(), "text", "rate", rating);

        log.info("User {} rated {} for {}", req.getUsername(), req.getId(), req.getRate());
    }

    public List<Integer> getRatingById(Long id) throws RatingException {
        Optional<List<DBObject>> ratings =
                objects.findAllByTypeNameAndAttributesTypeNameAndAttributesVal(Types.RATING.getValue(), "id",
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

    public void review(ReviewRequest req) {
        if (req.getRate() != null) {
            rate(new RatingRequest(req.getId(), req.getRate(), req.getUsername()));
        }

        Optional<DBObject> objFromDB = objects.findById(req.getId());

        DBObject review =
                db.findObjOrCreate(Types.REVIEW, objFromDB.get().getDescr() + req.getUsername());

        db.saveOrUpdateNewAttributeValue(req.getId().toString(), "text", "id", review);
        db.saveOrUpdateNewAttributeValue(req.getUsername(), "text", "username", review);
        db.saveOrUpdateNewAttributeValue(req.getTitle(), "text", "title", review);
        db.saveOrUpdateNewAttributeValue(req.getDescription(), "text", "description", review);
        db.saveOrUpdateNewAttributeValue(req.getRate().toString(), "text", "rate", review);
        db.saveOrUpdateNewAttributeValue(req.getDate(), "text", "created", review);

        log.info("User {} reviewed {} as {}", req.getUsername(), req.getId(), req.getTitle());
    }

    public List<ReviewRequest> getReviewsById(Long id) {
        Optional<List<DBObject>> reviews =
                objects.findAllByTypeNameAndAttributesTypeNameAndAttributesVal("review", "id",
                        id.toString());

        if (!reviews.isPresent()) {
            throw new RatingException("Reviews not found");
        }

        reviews.get().parallelStream().forEach((review) -> db.mapAttributes(review));

        List<ReviewRequest> res = new ArrayList<>();
        reviews.get().parallelStream().forEach((review) -> {
            ReviewRequest mappedReview = new ReviewRequest();
            mappedReview.setId(id);
            mappedReview.setUsername(review.getAttributeMap().get("username"));
            mappedReview.setTitle(review.getAttributeMap().get("title"));
            mappedReview.setDescription(review.getAttributeMap().get("description"));
            mappedReview.setRate(Integer.parseInt(review.getAttributeMap().get("rate")));
            mappedReview.setDate(review.getAttributeMap().get("created"));

            res.add(mappedReview);
        });

        return res;
    }
}
