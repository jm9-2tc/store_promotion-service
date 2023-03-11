package com.store.promotionservice;


import com.store.promotionservice.model.dto.PromotionDto;
import com.store.promotionservice.model.mappers.PromotionMapper;
import com.store.promotionservice.model.request.CalculateDiscountRequest;
import com.store.promotionservice.model.response.CalculateDiscountResponse;
import com.store.promotionservice.mothers.CalculateDiscountRequestMother;
import com.store.promotionservice.mothers.PromotionMother;

import com.store.promotionservice.repository.PromotionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PromotionRepository promotionRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @AfterEach
    void clearRepository() {
        promotionRepository.deleteAll();
    }

    @Nested
    @DisplayName("CRUD tests")
    class CrudTests {

        // CREATE:

        @Test
        @DisplayName("Can create promotion")
        void shouldCreatePromotion() throws Exception {
            String promotionJson = objectMapper.writeValueAsString(PromotionMother.getValidExample1());
            mockMvc.perform(MockMvcRequestBuilders.post("/api/promotions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(promotionJson)
            ).andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Reports error when creating promotion from missing data")
        void shouldNotCreatePromotionFromMissingData() throws Exception {
            String promotionJson = objectMapper.writeValueAsString(PromotionMother.getInvalidExample());
            mockMvc.perform(MockMvcRequestBuilders.post("/api/promotions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(promotionJson)
            ).andExpect(status().isBadRequest());
        }

        // READ:

        @Test
        @DisplayName("Can get promotion")
        void shouldGetPromotion() throws Exception {
            PromotionDto promotion = PromotionMother.getValidExample1();
            long id = promotionRepository.save(PromotionMapper.mapToEntity(promotion)).getId();

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/promotions/" + id))
                    .andExpect(status().isOk())
                    .andReturn();

            PromotionDto response = objectMapper.readValue(result.getResponse().getContentAsString(), PromotionDto.class);

            assertThat(response).isEqualTo(promotion);
        }

        @Test
        @DisplayName("Can get all promotions")
        void shouldGetAllPromotions() throws Exception {
            PromotionDto promotion1 = PromotionMother.getValidExample1();
            PromotionDto promotion2 = PromotionMother.getValidExample2();

            promotionRepository.save(PromotionMapper.mapToEntity(promotion1));
            promotionRepository.save(PromotionMapper.mapToEntity(promotion2));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/promotions"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andReturn();

            PromotionDto[] response = objectMapper.readValue(result.getResponse().getContentAsString(), PromotionDto[].class);

            assertThat(response).containsExactly(promotion1, promotion2);
        }

        // UPDATE:

        @Test
        @DisplayName("Can update promotion")
        void shouldUpdatePromotion() throws Exception {
            PromotionDto promotion = PromotionMother.getValidExample1();
            promotionRepository.save(PromotionMapper.mapToEntity(promotion));

            String promotionJson = objectMapper.writeValueAsString(promotion);

            mockMvc.perform(MockMvcRequestBuilders.put("/api/promotions/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(promotionJson)
            ).andExpect(status().isOk());
        }

        @Test
        @DisplayName("Reports error when updating promotion from missing data")
        void shouldNotUpdatePromotionFromMissingData() throws Exception {
            PromotionDto promotion = PromotionMother.getValidExample1();
            long id = promotionRepository.save(PromotionMapper.mapToEntity(promotion)).getId();

            String promotionJson = objectMapper.writeValueAsString(PromotionMother.getInvalidExample());

            mockMvc.perform(MockMvcRequestBuilders.put("/api/promotions/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(promotionJson)
            ).andExpect(status().isBadRequest());
        }

        // DELETE:

        @Test
        @DisplayName("Can delete promotion")
        void shouldDeletePromotion() throws Exception {
            PromotionDto promotion = PromotionMother.getValidExample1();
            promotionRepository.save(PromotionMapper.mapToEntity(promotion));

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/promotions/1")).andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Calculate discount tests")
    class CalculateDiscountTests {
        @Test
        @DisplayName("Can calculate discount")
        void shouldCalculateDiscount() throws Exception {
            PromotionDto promotion1 = PromotionMother.getValidExample1();
            PromotionDto promotion2 = PromotionMother.getValidExample2();
            PromotionDto promotion3 = PromotionMother.getValidExample3();
            PromotionDto promotion4 = PromotionMother.getValidExample4();

            promotionRepository.save(PromotionMapper.mapToEntity(promotion1));
            promotionRepository.save(PromotionMapper.mapToEntity(promotion2));
            promotionRepository.save(PromotionMapper.mapToEntity(promotion3));
            promotionRepository.save(PromotionMapper.mapToEntity(promotion4));

            List<CalculateDiscountResponse.DiscountData> discountsData = new ArrayList<>(5);
            discountsData.add(new CalculateDiscountResponse.DiscountData(1L, BigDecimal.valueOf(5.0f).setScale(2, RoundingMode.HALF_UP), 1L));
            discountsData.add(new CalculateDiscountResponse.DiscountData(2L, BigDecimal.valueOf(1.0f).setScale(2, RoundingMode.HALF_UP), 2L));
            discountsData.add(new CalculateDiscountResponse.DiscountData(3L, BigDecimal.valueOf(0.2f).setScale(2, RoundingMode.HALF_UP), 2L));
            discountsData.add(new CalculateDiscountResponse.DiscountData(4L, BigDecimal.valueOf(9.6).setScale(2, RoundingMode.HALF_UP), 4L));
            discountsData.add(new CalculateDiscountResponse.DiscountData(5L, BigDecimal.valueOf(0.0f).setScale(2, RoundingMode.HALF_UP), null));
            CalculateDiscountResponse expectedResponse = new CalculateDiscountResponse(discountsData);

            CalculateDiscountRequest request = CalculateDiscountRequestMother.getValidExample(true, "11111");
            String requestJson = objectMapper.writeValueAsString(request);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/promotions/calculate-discount")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson)
            ).andExpect(status().isOk()).andReturn();

            CalculateDiscountResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), CalculateDiscountResponse.class);
            assertThat(response).isEqualTo(expectedResponse);

            System.out.println(result.getResponse().getContentAsString());
        }
    }

}
