package com.store.promotionservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PromotionDto {
    @JsonProperty("id")
    private Long id;

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("club_card_required")
    private Boolean clubCardRequired;

    @JsonProperty("code")
    private String code;

    @JsonProperty("min_quantity")
    private Integer minQuantity;

    @JsonProperty("value")
    private BigDecimal value;

    @JsonProperty("value_percent")
    private Integer valuePercent;


    @JsonProperty("products_ids")
    private List<Long> productsIds;

    @JsonProperty("categories_ids")
    private List<Integer> categoriesIds;
}
