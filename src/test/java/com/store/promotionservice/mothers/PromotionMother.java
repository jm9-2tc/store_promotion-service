package com.store.promotionservice.mothers;

import com.store.promotionservice.model.dto.PromotionDto;
import com.store.promotionservice.model.entity.PromotionEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PromotionMother {
    /*private String name;

    private Boolean clubCardRequired;

    private String code;

    private Integer minQuantity;

    private BigDecimal value;

    private Integer valuePercent;

    private List<Long> productsIds;

    private List<Integer> categoriesIds;

    public PromotionMother(int promoNumber) {
        name = "promo " + promoNumber;
    }

    public PromotionMother requireClubCard(boolean clubCardRequired) {
        this.clubCardRequired = clubCardRequired;
        return this;
    }

    public PromotionMother withCode(String code) {
        this.code = code;
        return this;
    }

    public PromotionMother withMinQuantity(int minQty) {
        this.minQuantity = minQty;
        return this;
    }

    public PromotionMother withValue(float value) {
        this.value = BigDecimal.valueOf(value);
        return this;
    }

    public PromotionMother withPercentValue(int percentValue) {
        this.valuePercent = percentValue;
        return this;
    }

    public PromotionMother withProductIds() {
        productsIds = new ArrayList<>();
        productsIds.add(1L);
        productsIds.add(2L);
        productsIds.add(3L);
        return this;
    }

    public PromotionMother withCategoriesIds() {
        categoriesIds = new ArrayList<>();
        categoriesIds.add(1);
        categoriesIds.add(2);
        categoriesIds.add(3);
        return this;
    }*/

    public static PromotionEntity getValidEntityExample() {
        return new PromotionEntity(
                0L,
                "promo",
                true,
                "11111",
                3,
                BigDecimal.ONE,
                null,
                Collections.singletonList(1L),
                null
        );
    }

    public static PromotionDto getValidExample1() {
        return new PromotionDto(
                "promo 1",
                true,
                "11111",
                3,
                BigDecimal.valueOf(5.0f),
                null,
                Collections.singletonList(1L),
                null
        );
    }

    public static PromotionDto getValidExample2() {
        return new PromotionDto(
                "promo 2",
                false,
                null,
                null,
                null,
                25,
                null,
                Collections.singletonList(1)
        );
    }

    public static PromotionDto getInvalidExample() {
        return new PromotionDto();
    }

    /*
    public PromotionDto build() {
        return new PromotionDto(name, clubCardRequired, code, minQuantity, value, valuePercent, productsIds, categoriesIds);
    }*/
}
