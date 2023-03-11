package com.store.promotionservice.service;

import com.store.promotionservice.model.entity.PromotionEntity;
import com.store.promotionservice.model.request.CalculateDiscountRequest;
import com.store.promotionservice.model.response.CalculateDiscountResponse;
import com.store.promotionservice.repository.PromotionRepository;
import com.store.promotionservice.utils.Wrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiscountCalculatorService {
    private final PromotionRepository repository;

    public CalculateDiscountResponse calculate(CalculateDiscountRequest request) {
        List<PromotionEntity> promotions = repository.getMatching(request.getHasCard(), request.getPromotionCode());
        List<CalculateDiscountRequest.ProductInfo> products = request.getProducts();
        List<CalculateDiscountResponse.DiscountData> discounts = new ArrayList<>(products.size());

        Map<Long, PromotionsAndQuantityPair> productsDataMap = new HashMap<>(products.size());
        Map<Integer, PromotionsAndQuantityPair> categoriesDataMap = new HashMap<>(products.size());

        for (CalculateDiscountRequest.ProductInfo product : products) {
            int quantity = product.getQuantity();
            int categoryId = product.getCategoryId();

            productsDataMap.put(product.getId(), new PromotionsAndQuantityPair(quantity));

            PromotionsAndQuantityPair categoryPromotionsAndQuantity = categoriesDataMap.get(categoryId);
            if (categoryPromotionsAndQuantity == null) {
                categoryPromotionsAndQuantity = new PromotionsAndQuantityPair(quantity);
            } else {
                categoryPromotionsAndQuantity.quantity += quantity;
            }
            categoriesDataMap.put(categoryId, categoryPromotionsAndQuantity);
        }

        for(PromotionEntity promotion : promotions) {
            List<Long> productsIds = promotion.getProductsIds();
            List<Integer> categoriesIds = promotion.getCategoriesIds();
            Integer minQuantity = promotion.getMinQuantity();

            ValidatedPromotion validatedPromotion = new ValidatedPromotion(promotion);

            int quantity = 0;
            boolean isValid = false;

            if (productsIds != null) {
                for (Long id : productsIds) {
                    PromotionsAndQuantityPair productData = productsDataMap.get(id);

                    if (productData != null) {
                        isValid = true;
                        quantity += productData.quantity;
                        productData.promotions.add(validatedPromotion);
                    }
                }
            } else if (categoriesIds != null) {
                for (Integer id : categoriesIds) {
                    PromotionsAndQuantityPair categoryData = categoriesDataMap.get(id);

                    if (categoryData != null) {
                        isValid = true;
                        quantity += categoryData.quantity;
                        categoryData.promotions.add(validatedPromotion);
                    }
                }
            }

            if (minQuantity != null && minQuantity > quantity)
                isValid = false;

            validatedPromotion.valid = isValid;
        }

        for (CalculateDiscountRequest.ProductInfo product : products) {
            BigDecimal worth = product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
            BigDecimal biggestDiscount = BigDecimal.ZERO;
            Wrapper<Long> id = new Wrapper<>(null);

            biggestDiscount = findBiggestDiscount(biggestDiscount, worth, productsDataMap.get(product.getId()).promotions, id);
            biggestDiscount = findBiggestDiscount(biggestDiscount, worth, categoriesDataMap.get(product.getCategoryId()).promotions, id);

            biggestDiscount = biggestDiscount.max(BigDecimal.ZERO);
            biggestDiscount = biggestDiscount.setScale(2, RoundingMode.HALF_UP);

            discounts.add(new CalculateDiscountResponse.DiscountData(product.getId(), biggestDiscount, id.value));
        }

        CalculateDiscountResponse response = new CalculateDiscountResponse();
        response.setDiscounts(discounts);

        return response;
    }

    private static BigDecimal findBiggestDiscount(BigDecimal biggestDiscount, BigDecimal worth, List<ValidatedPromotion> categoryPromotions, Wrapper<Long> id) {
        for (ValidatedPromotion validatedPromotion : categoryPromotions) {
            if (!validatedPromotion.valid)
                continue;

            BigDecimal discount = validatedPromotion.promotion.getValue();

            if (discount == null) {
                Integer percentage = validatedPromotion.promotion.getValuePercent();
                if (percentage != null)
                    discount = worth.multiply(BigDecimal.valueOf(percentage / 100.0f));
            }

            if (discount != null && discount.compareTo(biggestDiscount) > 0) {
                biggestDiscount = discount;
                id.value = validatedPromotion.promotion.getId();
            }
        }
        return biggestDiscount;
    }

    private static final class PromotionsAndQuantityPair {
        public final List<ValidatedPromotion> promotions;
        public int quantity;

        public PromotionsAndQuantityPair(int quantity) {
            this.quantity = quantity;
            promotions = new ArrayList<>();
        }
    }

    private static final class ValidatedPromotion {
        public final PromotionEntity promotion;
        public boolean valid;

        public ValidatedPromotion(PromotionEntity promotion) {
            this.promotion = promotion;
            valid = false;
        }
    }
}
