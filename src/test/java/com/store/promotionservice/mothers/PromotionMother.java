package com.store.promotionservice.mothers;

import com.store.promotionservice.model.dto.PromotionDto;

import java.math.BigDecimal;
import java.util.Collections;

public class PromotionMother {

    public static PromotionDto getExample1() {
        return new PromotionDto(
                null,
                "promo 1",
                true,
                "11111",
                2,
                BigDecimal.valueOf(5.0f),
                null,
                Collections.singletonList(1L),
                null
        );
    }

    public static PromotionDto getExample2() {
        return new PromotionDto(
                null,
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

    public static PromotionDto getExample3() {
        return new PromotionDto(
                null,
                "promo 3",
                false,
                "abc",
                6,
                BigDecimal.valueOf(0.5f),
                null,
                null,
                Collections.singletonList(2)
        );
    }

    public static PromotionDto getExample4() {
        return new PromotionDto(
                null,
                "promo 4",
                true,
                null,
                null,
                null,
                30,
                Collections.singletonList(4L),
                null
        );
    }

    public static PromotionDto getInvalidExample() {
        return new PromotionDto();
    }
}
