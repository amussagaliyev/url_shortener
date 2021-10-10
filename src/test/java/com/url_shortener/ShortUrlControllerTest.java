package com.url_shortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.url_shortener.controller.api.dto.ShortenUrlResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShortUrlControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shortenUrlTest() throws Exception {
        ResultActions resultActions = mvc.perform(post("/api/url")
                .content("{\"url\": \"https://google.com\"}")
                .contentType(MediaType.APPLICATION_JSON));

        ShortenUrlResponse resp = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                ShortenUrlResponse.class);

        HttpGet httpGet = new HttpGet(resp.getShortenedUrl());

        try (CloseableHttpClient httpClient = HttpClients.custom().disableRedirectHandling().build()) {
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                Assertions.assertEquals(response.getHeader("Location").getValue(), resp.getOriginalUrl());
                Assertions.assertEquals(HttpStatus.MOVED_TEMPORARILY.value(), response.getCode());
            }
        }
    }
}
