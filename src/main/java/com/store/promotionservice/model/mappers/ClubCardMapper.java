package com.store.promotionservice.model.mappers;

import com.store.promotionservice.model.dto.ClubCardDto;
import com.store.promotionservice.model.entity.ClubCardEntity;

public class ClubCardMapper {
    public static ClubCardEntity mapToEntity(ClubCardDto dto) {
        if (dto == null)
            return null;
        ClubCardEntity entity = new ClubCardEntity();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    public static ClubCardDto mapToDto(ClubCardEntity entity) {
        if (entity == null)
            return null;
        return new ClubCardDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getEmail()
        );
    }
}
