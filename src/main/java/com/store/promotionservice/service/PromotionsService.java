package com.store.promotionservice.service;

import com.store.promotionservice.model.dto.PromotionDto;
import com.store.promotionservice.model.entity.PromotionEntity;
import com.store.promotionservice.model.mappers.PromotionMapper;
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
                .map(PromotionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public PromotionDto getPromotion(Long id) {
        return PromotionMapper.mapToDto(repository.findById(id).orElse(null));
    }

    public Long createPromotion(PromotionDto promotionDto) {
        return repository.save(PromotionMapper.mapToEntity(promotionDto)).getId();
    }

    public void updatePromotion(PromotionDto promotionDto, Long id) {
        PromotionEntity promotion = PromotionMapper.mapToEntity(promotionDto);
        promotion.setId(id);
        repository.save(promotion);
    }

    public void deletePromotion(Long id) {
        repository.deleteById(id);
    }


}
