package com.url_shortener;

import com.url_shortener.repository.UrlHashRepository;
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

import java.net.URL;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CacheTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @SpyBean
    private UrlHashService urlHashService;

    @Autowired
    @SpyBean
    private UrlHashRepository urlHashRepository;

    @Captor
    private ArgumentCaptor<String> hashCaptor;

    private final String request = "{\"url\": \"https://google.com\"}";

    @Test
    public void shouldNotHitDb() throws Exception {
        mvc.perform(post("/api/url")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(urlHashService, Mockito.times(1))
                .storeUrlHash(argThat(any(URL.class)), hashCaptor.capture());

        urlHashService.getOriginalUrlByHash(hashCaptor.getValue());
        Mockito.verify(urlHashRepository, Mockito.times(0)).getByHash(hashCaptor.getValue());
    }

    @Test
    public void shouldHitDbAfterExpired() throws Exception {
        mvc.perform(post("/api/url")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(urlHashService).storeUrlHash(argThat(any(URL.class)), hashCaptor.capture());

        Thread.sleep(5000);

        urlHashService.getOriginalUrlByHash(hashCaptor.getValue());
        Mockito.verify(urlHashRepository, Mockito.times(1)).getByHash(argThat(any(String.class)));
    }

    @Test
    public void shouldRemoveRecordAfterExpired() throws Exception {
        mvc.perform(post("/api/url")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(urlHashService, Mockito.times(1)).storeUrlHash(argThat(any(URL.class)), hashCaptor.capture());

        Thread.sleep(6000);
        assertNull(urlHashRepository.getByHash(hashCaptor.capture()).orElse(null));
    }

}
