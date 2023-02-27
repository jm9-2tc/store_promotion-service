package com.store.promotionservice.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "promotions")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class PromotionEntity {
    @Transient
    public static final String SEQUENCE_NAME = "promotions_sequence";

    @MongoId(FieldType.INT64)
    @Field("_id")
    private Long id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("club_card_required")
    private Boolean clubCardRequired;

    @Field("code")
    private String code;

    @Field("min_quantity")
    private Integer minQuantity;

    @Field("value")
    private BigDecimal value;

    @Field("value_percent")
    private Integer valuePercent;


    @Field("products_ids")
    private List<Long> productsIds;

    @Field("categories_ids")
    private List<Integer> categoriesIds;
}
