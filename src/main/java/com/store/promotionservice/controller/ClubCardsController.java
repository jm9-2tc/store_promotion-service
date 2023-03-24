package com.store.promotionservice.controller;

import com.store.promotionservice.model.dto.ClubCardDto;
import com.store.promotionservice.service.ClubCardsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/club-cards")
public class ClubCardsController {
    private final ClubCardsService service;

    @GetMapping
    public ResponseEntity<List<ClubCardDto>> getAllClubCards() {
        return new ResponseEntity<>(service.getAllClubCards(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubCardDto> getClubCard(@PathVariable Long id) {
        ClubCardDto product = service.getClubCard(id);
        if (product == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<ClubCardDto> createClubCard(@Valid @RequestBody ClubCardDto productDto, UriComponentsBuilder builder) {
        Long id = service.createClubCard(productDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/api/club-cards/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateClubCard(@Valid @RequestBody ClubCardDto productDto) {
        service.updateClubCard(productDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClubCard(@PathVariable Long id) {
        service.deleteClubCard(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
