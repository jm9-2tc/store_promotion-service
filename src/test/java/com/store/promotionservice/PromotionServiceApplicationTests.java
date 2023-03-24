package com.store.promotionservice;


import com.store.promotionservice.model.dto.ClubCardDto;
import com.store.promotionservice.model.dto.GiftCardDto;
import com.store.promotionservice.model.dto.PromotionDto;
import com.store.promotionservice.model.mappers.ClubCardMapper;
import com.store.promotionservice.model.mappers.GiftCardMapper;
import com.store.promotionservice.model.mappers.PromotionMapper;
import com.store.promotionservice.model.request.CalculateDiscountRequest;
import com.store.promotionservice.model.response.CalculateDiscountResponse;
import com.store.promotionservice.mothers.CalculateDiscountRequestMother;
import com.store.promotionservice.mothers.ClubCardMother;
import com.store.promotionservice.mothers.GiftCardMother;
import com.store.promotionservice.mothers.PromotionMother;

import com.store.promotionservice.repository.ClubCardRepository;
import com.store.promotionservice.repository.GiftCardRepository;
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
class PromotionServiceApplicationTests {
    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ClubCardRepository clubCardRepository;

    @Autowired
    private GiftCardRepository giftCardRepository;


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @AfterEach
    void clearRepository() {
        promotionRepository.deleteAll();
    }

    @Nested
    @DisplayName("Promotions CRUD tests")
    class PromotionsCrudTests {

        // CREATE:

        @Test
        @DisplayName("Can create promotion")
        void shouldCreatePromotion() throws Exception {
            String promotionJson = objectMapper.writeValueAsString(PromotionMother.getExample1());
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
            PromotionDto promotion = PromotionMother.getExample1();
            long id = promotionRepository.save(PromotionMapper.mapToEntity(promotion)).getId();

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/promotions/" + id))
                    .andExpect(status().isOk())
                    .andReturn();

            PromotionDto response = objectMapper.readValue(result.getResponse().getContentAsString(), PromotionDto.class);

            assertThat(response)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(promotion);
        }

        @Test
        @DisplayName("Can get all promotions")
        void shouldGetAllPromotions() throws Exception {
            PromotionDto promotion1 = PromotionMother.getExample1();
            PromotionDto promotion2 = PromotionMother.getExample2();

            promotionRepository.save(PromotionMapper.mapToEntity(promotion1));
            promotionRepository.save(PromotionMapper.mapToEntity(promotion2));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/promotions"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andReturn();

            PromotionDto[] response = objectMapper.readValue(result.getResponse().getContentAsString(), PromotionDto[].class);

            assertThat(response)
                    .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                    .containsExactly(promotion1, promotion2);
        }

        // UPDATE:

        @Test
        @DisplayName("Can update promotion")
        void shouldUpdatePromotion() throws Exception {
            PromotionDto promotion = PromotionMother.getExample1();
            long id = promotionRepository.save(PromotionMapper.mapToEntity(promotion)).getId();

            String promotionJson = objectMapper.writeValueAsString(promotion);

            mockMvc.perform(MockMvcRequestBuilders.put("/api/promotions/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(promotionJson)
            ).andExpect(status().isOk());
        }

        @Test
        @DisplayName("Reports error when updating promotion from missing data")
        void shouldNotUpdatePromotionFromMissingData() throws Exception {
            PromotionDto promotion = PromotionMother.getExample1();
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
            PromotionDto promotion = PromotionMother.getExample1();
            promotionRepository.save(PromotionMapper.mapToEntity(promotion));

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/promotions/1")).andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Club Cards CRUD tests")
    class ClubCardsCrudTests {

        // CREATE:

        @Test
        @DisplayName("Can create club card")
        void shouldCreateClubCard() throws Exception {
            String clubCardJson = objectMapper.writeValueAsString(ClubCardMother.getExample1());
            mockMvc.perform(MockMvcRequestBuilders.post("/api/club-cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(clubCardJson)
            ).andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Reports error when creating club card from missing data")
        void shouldNotCreateClubCardFromMissingData() throws Exception {
            String clubCardJson = objectMapper.writeValueAsString(ClubCardMother.getInvalidExample());
            mockMvc.perform(MockMvcRequestBuilders.post("/api/club-cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(clubCardJson)
            ).andExpect(status().isBadRequest());
        }

        // READ:

        @Test
        @DisplayName("Can get club card")
        void shouldGetClubCard() throws Exception {
            ClubCardDto clubCard = ClubCardMother.getExample1();
            long id = clubCardRepository.save(ClubCardMapper.mapToEntity(clubCard)).getId();

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/club-cards/" + id))
                    .andExpect(status().isOk())
                    .andReturn();

            ClubCardDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ClubCardDto.class);

            assertThat(response).isEqualTo(clubCard);
        }

        @Test
        @DisplayName("Can get all club cards")
        void shouldGetAllClubCards() throws Exception {
            ClubCardDto clubCard1 = ClubCardMother.getExample1();
            ClubCardDto clubCard2 = ClubCardMother.getExample2();

            clubCardRepository.save(ClubCardMapper.mapToEntity(clubCard1));
            clubCardRepository.save(ClubCardMapper.mapToEntity(clubCard2));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/club-cards"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andReturn();

            ClubCardDto[] response = objectMapper.readValue(result.getResponse().getContentAsString(), ClubCardDto[].class);

            assertThat(response).containsExactly(clubCard1, clubCard2);
        }

        // UPDATE:

        @Test
        @DisplayName("Can update club card")
        void shouldUpdateClubCard() throws Exception {
            ClubCardDto clubCard = ClubCardMother.getExample1();
            clubCardRepository.save(ClubCardMapper.mapToEntity(clubCard));

            String clubCardJson = objectMapper.writeValueAsString(clubCard);

            mockMvc.perform(MockMvcRequestBuilders.put("/api/club-cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(clubCardJson)
            ).andExpect(status().isOk());
        }

        @Test
        @DisplayName("Reports error when updating club card from missing data")
        void shouldNotUpdateClubCardFromMissingData() throws Exception {
            ClubCardDto clubCard = ClubCardMother.getExample1();
            clubCardRepository.save(ClubCardMapper.mapToEntity(clubCard));

            String clubCardJson = objectMapper.writeValueAsString(ClubCardMother.getInvalidExample());

            mockMvc.perform(MockMvcRequestBuilders.put("/api/club-cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(clubCardJson)
            ).andExpect(status().isBadRequest());
        }

        // DELETE:

        @Test
        @DisplayName("Can delete club card")
        void shouldDeleteClubCard() throws Exception {
            ClubCardDto clubCard = ClubCardMother.getExample1();
            clubCardRepository.save(ClubCardMapper.mapToEntity(clubCard));

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/club-cards/1")).andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Gift Cards CRUD tests")
    class GiftCardsCrudTests {

        // CREATE:

        @Test
        @DisplayName("Can create gift card")
        void shouldCreateGiftCard() throws Exception {
            String giftCardJson = objectMapper.writeValueAsString(GiftCardMother.getExample1());
            mockMvc.perform(MockMvcRequestBuilders.post("/api/gift-cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(giftCardJson)
            ).andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Reports error when creating gift card from missing data")
        void shouldNotCreateGiftCardFromMissingData() throws Exception {
            String giftCardJson = objectMapper.writeValueAsString(GiftCardMother.getInvalidExample());
            mockMvc.perform(MockMvcRequestBuilders.post("/api/gift-cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(giftCardJson)
            ).andExpect(status().isBadRequest());
        }

        // READ:

        @Test
        @DisplayName("Can get gift card")
        void shouldGetGiftCard() throws Exception {
            GiftCardDto giftCard = GiftCardMother.getExample1();
            long id = giftCardRepository.save(GiftCardMapper.mapToEntity(giftCard)).getId();

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/gift-cards/" + id))
                    .andExpect(status().isOk())
                    .andReturn();

            GiftCardDto response = objectMapper.readValue(result.getResponse().getContentAsString(), GiftCardDto.class);

            assertThat(response).isEqualTo(giftCard);
        }

        @Test
        @DisplayName("Can get all gift cards")
        void shouldGetAllGiftCards() throws Exception {
            GiftCardDto giftCard1 = GiftCardMother.getExample1();
            GiftCardDto giftCard2 = GiftCardMother.getExample2();

            giftCardRepository.save(GiftCardMapper.mapToEntity(giftCard1));
            giftCardRepository.save(GiftCardMapper.mapToEntity(giftCard2));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/gift-cards"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andReturn();

            GiftCardDto[] response = objectMapper.readValue(result.getResponse().getContentAsString(), GiftCardDto[].class);

            assertThat(response).containsExactly(giftCard1, giftCard2);
        }

        // UPDATE:

        @Test
        @DisplayName("Can update gift card")
        void shouldUpdateGiftCard() throws Exception {
            GiftCardDto giftCard = GiftCardMother.getExample1();
            giftCardRepository.save(GiftCardMapper.mapToEntity(giftCard));

            String giftCardJson = objectMapper.writeValueAsString(giftCard);

            mockMvc.perform(MockMvcRequestBuilders.put("/api/gift-cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(giftCardJson)
            ).andExpect(status().isOk());
        }

        @Test
        @DisplayName("Reports error when updating gift card from missing data")
        void shouldNotUpdateGiftCardFromMissingData() throws Exception {
            GiftCardDto giftCard = GiftCardMother.getExample1();
            giftCardRepository.save(GiftCardMapper.mapToEntity(giftCard));

            String giftCardJson = objectMapper.writeValueAsString(GiftCardMother.getInvalidExample());

            mockMvc.perform(MockMvcRequestBuilders.put("/api/gift-cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(giftCardJson)
            ).andExpect(status().isBadRequest());
        }

        // DELETE:

        @Test
        @DisplayName("Can delete gift card")
        void shouldDeleteGiftCard() throws Exception {
            GiftCardDto giftCard = GiftCardMother.getExample1();
            giftCardRepository.save(GiftCardMapper.mapToEntity(giftCard));

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/gift-cards/1")).andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Calculate discount tests")
    class CalculateDiscountTests {
        @Test
        @DisplayName("Can calculate discount")
        void shouldCalculateDiscount() throws Exception {
            PromotionDto promotion1 = PromotionMother.getExample1();
            PromotionDto promotion2 = PromotionMother.getExample2();
            PromotionDto promotion3 = PromotionMother.getExample3();
            PromotionDto promotion4 = PromotionMother.getExample4();

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

            CalculateDiscountRequest request = CalculateDiscountRequestMother.getExample(true, "11111");
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
