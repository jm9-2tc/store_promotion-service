package com.store.promotionservice.mothers;

import com.store.promotionservice.model.request.CalculateDiscountRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculateDiscountRequestMother {
    public static CalculateDiscountRequest getValidExample(boolean hasCard, String promotionCode) {
        List<CalculateDiscountRequest.ProductInfo> products = new ArrayList<>(4);

        products.add(new CalculateDiscountRequest.ProductInfo(1L, 1, 2, BigDecimal.valueOf(5.5f)));
        products.add(new CalculateDiscountRequest.ProductInfo(2L, 1, 2, BigDecimal.valueOf(2.0f)));
        products.add(new CalculateDiscountRequest.ProductInfo(3L, 1, 1, BigDecimal.valueOf(0.8f)));
        products.add(new CalculateDiscountRequest.ProductInfo(4L, 2, 4, BigDecimal.valueOf(8.0f)));
        products.add(new CalculateDiscountRequest.ProductInfo(5L, 2, 3, BigDecimal.valueOf(3.5f)));

        return new CalculateDiscountRequest(products, hasCard, promotionCode);
    }
}
