package com.store.promotionservice.service;

import com.store.promotionservice.model.dto.PromotionDto;
import com.store.promotionservice.model.entity.PromotionEntity;
import com.store.promotionservice.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionsService {

    private final PromotionRepository repository;

    public List<PromotionDto> getAllPromotions() {
        List<PromotionEntity> promotions = repository.findAll();
        return promotions
                .stream()
                .map(PromotionsService::mapToDto)
                .collect(Collectors.toList());
    }

    public PromotionDto getPromotion(Long id) {
        return mapToDto(repository.findById(id).orElse(null));
    }

    public Long createPromotion(PromotionDto promotionDto) {
        return repository.save(mapToEntity(promotionDto)).getId();
    }

    public void updatePromotion(PromotionDto promotionDto, Long id) {
        PromotionEntity promotion = mapToEntity(promotionDto);
        promotion.setId(id);
        repository.save(promotion);
    }

    public void deletePromotion(Long id) {
        repository.deleteById(id);
    }

    private static PromotionEntity mapToEntity(PromotionDto dto) {
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

    private static PromotionDto mapToDto(PromotionEntity entity) {
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
