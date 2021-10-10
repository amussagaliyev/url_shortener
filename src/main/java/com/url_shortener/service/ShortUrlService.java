package com.url_shortener.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShortUrlService {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${invalidUrlPage}")
    private String invalidUrlPage;

    public URL buildShortUrl(String hash) {
        try {
            return new URL(baseUrl + "/" + hash);
        } catch (MalformedURLException e) {
            try {
                return new URL(invalidUrlPage);
            } catch (MalformedURLException e2) {
                log.error("buildShortUrl failed", e2);
                throw new RuntimeException(e2);
            }
        }
    }
}
