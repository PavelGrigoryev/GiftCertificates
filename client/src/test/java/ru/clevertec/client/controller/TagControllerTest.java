package ru.clevertec.client.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.client.annotation.WireMockControllerTest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static ru.clevertec.client.util.TestConstants.APPLICATION_JSON;
import static ru.clevertec.client.util.TestConstants.CONTENT_TYPE;
import static ru.clevertec.client.util.TestConstants.TAGS;

@WireMockControllerTest
@RequiredArgsConstructor
class TagControllerTest {

    private final MockMvc mockMvc;

    @Test
    @DisplayName("test should return expected json and status 200")
    void testShouldReturnExpectedJsonAndStatus200() throws Exception {
        String json = """
                {
                  "id": 3,
                  "name": "Sprite"
                }
                """;

        stubFor(get(urlEqualTo(TAGS + 3))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

        mockMvc.perform(MockMvcRequestBuilders.get(TAGS + 3))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

}
