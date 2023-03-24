package com.store.promotionservice.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "club_cards")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class ClubCardEntity {
    @MongoId(FieldType.INT64)
    @Field("_id")
    private Long id;

    @NotNull
    @Field("first_name")
    private String firstName;

    @NotNull
    @Field("last_name")
    private String lastName;

    @NotNull
    @Field("phone")
    private String phone;

    @NotNull
    @Field("email")
    private String email;
}
