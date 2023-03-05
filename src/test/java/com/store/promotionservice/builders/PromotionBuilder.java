package com.store.promotionservice.builders;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.store.promotionservice.model.dto.PromotionDto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PromotionBuilder {
    private String name;

    private Boolean clubCardRequired;

    private String code;

    private Integer minQuantity;

    private BigDecimal value;

    private Integer valuePercent;

    private List<Long> productsIds;

    private List<Integer> categoriesIds;

    public PromotionBuilder(int promoNumber) {
        name = "promo " + promoNumber;
    }

    public PromotionBuilder requireClubCard(boolean clubCardRequired) {
        this.clubCardRequired = clubCardRequired;
        return this;
    }

    public PromotionBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public PromotionBuilder withMinQuantity(int minQty) {
        this.minQuantity = minQty;
        return this;
    }

    public PromotionBuilder withValue(float value) {
        this.value = BigDecimal.valueOf(value);
        return this;
    }

    public PromotionBuilder withPercentValue(int percentValue) {
        this.valuePercent = percentValue;
        return this;
    }

    public PromotionBuilder withProductIds() {
        productsIds = new ArrayList<>();
        productsIds.add((long) 1);
        productsIds.add((long) 2);
        productsIds.add((long) 3);
        return this;
    }

    public PromotionBuilder withCategoriesIds() {
        categoriesIds = new ArrayList<>();
        categoriesIds.add(1);
        categoriesIds.add(2);
        categoriesIds.add(3);
        return this;
    }

    public static PromotionDto getDefault() {
        return new PromotionDto(
                "promo",
                true,
                "11111",
                3,
                BigDecimal.ONE,
                null,
                Collections.singletonList((long) 1),
                null
        );
    }

    public PromotionDto build() {
        return new PromotionDto(name, clubCardRequired, code, minQuantity, value, valuePercent, productsIds, categoriesIds);
    }
}
