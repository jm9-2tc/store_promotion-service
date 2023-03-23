package com.store.promotionservice.model.mappers;

import com.store.promotionservice.model.dto.GiftCardDto;
import com.store.promotionservice.model.entity.GiftCardEntity;

public class GiftCardMapper {
    public static GiftCardEntity mapToEntity(GiftCardDto dto) {
        if (dto == null)
            return null;
        GiftCardEntity entity = new GiftCardEntity();
        entity.setId(dto.getId());
        entity.setWorth(dto.getWorth());
        return entity;
    }

    public static GiftCardDto mapToDto(GiftCardEntity entity) {
        if (entity == null)
            return null;
        return new GiftCardDto(
                entity.getId(),
                entity.getWorth()
        );
    }
}
