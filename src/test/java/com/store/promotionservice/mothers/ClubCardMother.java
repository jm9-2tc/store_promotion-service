package com.store.promotionservice.mothers;

import com.store.promotionservice.model.dto.ClubCardDto;

public class ClubCardMother {
    public static ClubCardDto getExample1() {
        return new ClubCardDto(
                11111L,
                "Kinga",
                "Pacek",
                "+48512128256",
                "kingapacek@tm1.edu.pl"
        );
    }

    public static ClubCardDto getExample2() {
        return new ClubCardDto(
                12345L,
                "Janusz",
                "Kowalski",
                "+48123666123",
                "janusz@poczta.pl"
        );
    }

    public static ClubCardDto getInvalidExample() {
        return new ClubCardDto();
    }
}
