package com.store.promotionservice.service;

import com.store.promotionservice.exceptions.ErrorCommunicates;
import com.store.promotionservice.exceptions.RequestProcessingException;
import com.store.promotionservice.model.dto.ClubCardDto;
import com.store.promotionservice.model.entity.ClubCardEntity;
import com.store.promotionservice.model.mappers.ClubCardMapper;
import com.store.promotionservice.repository.ClubCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubCardsService {

    private final ClubCardRepository repository;

    public List<ClubCardDto> getAllClubCards() {
        List<ClubCardEntity> clubCards = repository.findAll();
        return clubCards
                .stream()
                .map(ClubCardMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public ClubCardDto getClubCard(Long id) {
        return ClubCardMapper.mapToDto(repository.findById(id).orElse(null));
    }

    public Long createClubCard(ClubCardDto clubCardDto) {
        if (repository.findById(clubCardDto.getId()).isPresent()) {
            throw new RequestProcessingException(ErrorCommunicates.entityWithIdExists("ClubCard", clubCardDto.getId()), HttpStatus.CONFLICT);
        }
        return repository.save(ClubCardMapper.mapToEntity(clubCardDto)).getId();
    }

    public void updateClubCard(ClubCardDto clubCardDto) {
        ClubCardEntity clubCard = ClubCardMapper.mapToEntity(clubCardDto);
        repository.save(clubCard);
    }

    public void deleteClubCard(Long id) {
        repository.deleteById(id);
    }
}
