package com.store.promotionservice.repository;

import com.store.promotionservice.model.entity.GiftCardEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GiftCardRepository extends MongoRepository<GiftCardEntity, Long> { }
