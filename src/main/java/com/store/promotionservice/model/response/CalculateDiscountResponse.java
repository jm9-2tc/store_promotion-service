package com.store.promotionservice.model.response;

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
public class CalculateDiscountResponse {
    @NotNull
    @JsonProperty("discounts")
    private List<DiscountData> discounts;


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DiscountData {
        @JsonProperty("product_id")
        private Long productId;

        @JsonProperty("value")
        private BigDecimal value;
    }
}
