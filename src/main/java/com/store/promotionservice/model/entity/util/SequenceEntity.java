package com.store.promotionservice.model.entity.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "_sequences")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class SequenceEntity {
    @MongoId(FieldType.STRING)
    @Field("_id")
    private String id;

    @Field("value")
    private Long value;
}
