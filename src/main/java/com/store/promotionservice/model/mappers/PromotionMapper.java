package com.store.promotionservice.model.mappers;

import com.store.promotionservice.model.dto.PromotionDto;
import com.store.promotionservice.model.entity.PromotionEntity;

public class PromotionMapper {
    public static PromotionEntity mapToEntity(PromotionDto dto) {
        if (dto == null)
            return null;
        PromotionEntity entity = new PromotionEntity();
        entity.setName(dto.getName());
        entity.setClubCardRequired(dto.getClubCardRequired());
        entity.setCode(dto.getCode());
        entity.setMinQuantity(dto.getMinQuantity());
        entity.setValue(dto.getValue());
        entity.setValuePercent(dto.getValuePercent());
        entity.setProductsIds(dto.getProductsIds());
        entity.setCategoriesIds(dto.getCategoriesIds());
        return entity;
    }

    public static PromotionDto mapToDto(PromotionEntity entity) {
        if (entity == null)
            return null;
        return new PromotionDto(
                entity.getName(),
                entity.getClubCardRequired(),
                entity.getCode(),
                entity.getMinQuantity(),
                entity.getValue(),
                entity.getValuePercent(),
                entity.getProductsIds(),
                entity.getCategoriesIds());
    }
}
