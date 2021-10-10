package com.url_shortener;

import com.url_shortener.service.HashGenerator;
import com.url_shortener.service.UrlHashService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URL;

import static org.hamcrest.Matchers.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @SpyBean
    private HashGenerator hashGenerator;

    @Autowired
    @SpyBean
    private UrlHashService urlHashService;

    @Captor
    private ArgumentCaptor<String> hashCaptor;

    @Test
    public void shortenUrlTest() throws Exception {
        ResultActions resultActions = mvc.perform(post("/api/url")
                .content("{\"url\": \"https://google.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(hashGenerator, Mockito.times(1)).generate();
        Mockito.verify(urlHashService).storeUrlHash(argThat(any(URL.class)), hashCaptor.capture());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl", is("https://google.com")))
                .andExpect(jsonPath("$.shortenedUrl", is("http://localhost:8080/s/" + hashCaptor.getValue())))
        ;
    }

    @Test
    public void getAllUrlsTest() throws Exception {
        mvc.perform(post("/api/url")
                .content("{\"url\": \"https://google.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(post("/api/url")
                .content("{\"url\": \"https://facebook.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(urlHashService, Mockito.times(2)).storeUrlHash(argThat(any(URL.class)), hashCaptor.capture());

        mvc.perform(get("/api/url")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.[0].originalUrl", is("https://google.com")))
                .andExpect(jsonPath("$.[0].shortenedUrl", containsString("http://localhost:8080/s/" + hashCaptor.getAllValues().get(0))))
                .andExpect(jsonPath("$.[1].originalUrl", is("https://facebook.com")))
                .andExpect(jsonPath("$.[1].shortenedUrl", containsString("http://localhost:8080/s/" + hashCaptor.getAllValues().get(1))))
        ;
    }
}
