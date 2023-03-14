package com.store.promotionservice.repository;

import com.store.promotionservice.model.entity.ClubCardEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClubCardRepository extends MongoRepository<ClubCardEntity, Long> { }
