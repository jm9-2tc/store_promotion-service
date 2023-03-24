package com.store.promotionservice.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;

@Document(collection = "gift_cards")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class GiftCardEntity {
    @MongoId(FieldType.INT64)
    @Field("_id")
    private Long id;

    @NotNull
    @Field("worth")
    private BigDecimal worth;
}
