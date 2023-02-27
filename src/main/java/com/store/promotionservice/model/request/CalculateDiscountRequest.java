package com.store.promotionservice.model.request;

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
public class CalculateDiscountRequest {
    @NotNull
    @JsonProperty("products")
    private List<ProductInfo> products;

    @NotNull
    @JsonProperty("has_card")
    private Boolean hasCard;

    @NotNull
    @JsonProperty("promotion_code")
    private String promotionCode;
    

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ProductInfo {
        @NotNull
        @JsonProperty("id")
        private Long id;

        @NotNull
        @JsonProperty("category_id")
        private Integer categoryId;

        @NotNull
        @JsonProperty("quantity")
        private Integer quantity;

        @NotNull
        @JsonProperty("price")
        private BigDecimal price;
    }
}