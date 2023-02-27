package com.store.promotionservice.service.util;

import com.store.promotionservice.model.entity.util.SequenceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SequenceGenerator {
    private final MongoOperations mongoOperations;

    public long generateSequence(String seqName) {
        SequenceEntity counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("value",1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                SequenceEntity.class
        );
        if (counter == null || counter.getValue() == null)
            return 1;
        return counter.getValue();
    }
}
