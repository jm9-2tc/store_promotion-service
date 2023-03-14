package com.store.promotionservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClubCardDto {
    @NotNull
    @JsonProperty("id")
    private Long id;

    @NotNull
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @JsonProperty("phone")
    private String phone;

    @NotNull
    @JsonProperty("email")
    private String email;
}
