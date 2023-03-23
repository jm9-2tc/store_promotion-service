package com.store.promotionservice.controller;

import com.store.promotionservice.model.dto.GiftCardDto;
import com.store.promotionservice.service.GiftCardsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gift-cards")
public class GiftCardsController {
    private final GiftCardsService service;

    @GetMapping
    public ResponseEntity<List<GiftCardDto>> getAllGiftCards() {
        return new ResponseEntity<>(service.getAllGiftCards(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCardDto> getGiftCard(@PathVariable Long id) {
        GiftCardDto product = service.getGiftCard(id);
        if (product == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<GiftCardDto> createGiftCard(@Valid @RequestBody GiftCardDto productDto, UriComponentsBuilder builder) {
        Long id = service.createGiftCard(productDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/api/products/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateGiftCard(@Valid @RequestBody GiftCardDto productDto) {
        service.updateGiftCard(productDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftCard(@PathVariable Long id) {
        service.deleteGiftCard(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/decrease-worth/{id}")
    public ResponseEntity<Void> decreaseGiftCardWorth(@PathVariable Long id, @RequestParam("worth") BigDecimal worth) {
        service.decreaseGiftCardWorth(id, worth);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
