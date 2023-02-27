package com.store.promotionservice.model.listeners;

import com.store.promotionservice.model.entity.PromotionEntity;
import com.store.promotionservice.service.util.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionListener extends AbstractMongoEventListener<PromotionEntity> {
    private final SequenceGenerator sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<PromotionEntity> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(sequenceGenerator.generateSequence(PromotionEntity.SEQUENCE_NAME));
        }
    }
}


