package com.store.promotionservice.controller;

import com.store.promotionservice.model.dto.PromotionDto;
import com.store.promotionservice.model.request.CalculateDiscountRequest;
import com.store.promotionservice.model.response.CalculateDiscountResponse;
import com.store.promotionservice.service.PromotionsService;
import com.store.promotionservice.service.DiscountCalculatorService;
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
@RequestMapping("/api/promotions")
public class PromotionsController {
    private final PromotionsService service;
    private final DiscountCalculatorService discountCalculatorService;

    @GetMapping
    public ResponseEntity<List<PromotionDto>> getAllPromotions() {
        return new ResponseEntity<>(service.getAllPromotions(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDto> getPromotion(@PathVariable Long id) {
        PromotionDto promotion = service.getPromotion(id);
        if (promotion == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(promotion, HttpStatus.OK);
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<PromotionDto> createPromotion(@Valid @RequestBody PromotionDto promotionDto, UriComponentsBuilder builder) {
        Long id = service.createPromotion(promotionDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/api/promotions/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updatePromotion(@Valid @RequestBody PromotionDto promotionDto, @PathVariable Long id) {
        service.updatePromotion(promotionDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        service.deletePromotion(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping(path = "calculate-discount", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<CalculateDiscountResponse> calculateDiscount(@Valid @RequestBody CalculateDiscountRequest request) {
        CalculateDiscountResponse response = discountCalculatorService.calculate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
