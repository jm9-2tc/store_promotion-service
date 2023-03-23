package com.store.promotionservice.mothers;

import com.store.promotionservice.model.dto.GiftCardDto;

import java.math.BigDecimal;

public class GiftCardMother {
    public static GiftCardDto getExample1() {
        return new GiftCardDto(
                11111L,
                BigDecimal.valueOf(21.37f)
        );
    }

    public static GiftCardDto getExample2() {
        return new GiftCardDto(
                12345L,
                BigDecimal.valueOf(69.9f)
        );
    }

    public static GiftCardDto getInvalidExample() {
        return new GiftCardDto();
    }
}
