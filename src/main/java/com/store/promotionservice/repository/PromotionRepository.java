package com.store.promotionservice.repository;

import com.store.promotionservice.model.entity.PromotionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PromotionRepository extends MongoRepository<PromotionEntity, Long> {

    @Query("{ 'club_card_required': {'$lte':  ?0}, 'code': {'$in': [ null, ?1 ]}}")
    List<PromotionEntity> getMatching(boolean requireCard, String code);
}
