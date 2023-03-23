package com.store.promotionservice.service;

import com.store.promotionservice.exceptions.ErrorCommunicates;
import com.store.promotionservice.exceptions.RequestProcessingException;
import com.store.promotionservice.model.dto.GiftCardDto;
import com.store.promotionservice.model.entity.GiftCardEntity;
import com.store.promotionservice.model.mappers.GiftCardMapper;
import com.store.promotionservice.repository.GiftCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCardsService {

    private final GiftCardRepository repository;

    public List<GiftCardDto> getAllGiftCards() {
        List<GiftCardEntity> giftCards = repository.findAll();
        return giftCards
                .stream()
                .map(GiftCardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public GiftCardDto getGiftCard(Long id) {
        return GiftCardMapper.mapToDto(repository.findById(id).orElse(null));
    }

    public Long createGiftCard(GiftCardDto giftCardDto) {
        if (repository.findById(giftCardDto.getId()).isPresent()) {
            throw new RequestProcessingException(ErrorCommunicates.entityWithIdExists("GiftCard", giftCardDto.getId()), HttpStatus.CONFLICT);
        }
        return repository.save(GiftCardMapper.mapToEntity(giftCardDto)).getId();
    }

    public void updateGiftCard(GiftCardDto giftCardDto) {
        GiftCardEntity giftCard = GiftCardMapper.mapToEntity(giftCardDto);
        repository.save(giftCard);
    }

    public void deleteGiftCard(Long id) {
        repository.deleteById(id);
    }


    public void decreaseGiftCardWorth(Long id, BigDecimal worth) {
        GiftCardDto giftCardDto = GiftCardMapper.mapToDto(repository.findById(id).orElse(null));
        if (giftCardDto == null)
            throw new RequestProcessingException(ErrorCommunicates.entityWithIdDoesntExist("Gift card", id), HttpStatus.NOT_FOUND);
        giftCardDto.setWorth(giftCardDto.getWorth().subtract(worth).max(BigDecimal.ZERO));
    }
}
