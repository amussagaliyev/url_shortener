package com.url_shortener.controller.api;

import com.url_shortener.controller.api.dto.ShortenUrlRequest;
import com.url_shortener.controller.api.dto.ShortenUrlResponse;
import com.url_shortener.model.UrlHash;
import com.url_shortener.service.HashGenerator;
import com.url_shortener.service.ShortUrlService;
import com.url_shortener.service.UrlHashService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class UrlShortenerController {

    private final UrlHashService urlHashService;
    private final ShortUrlService shortUrlService;
    private final HashGenerator hashGenerator;

    @PostMapping("/url")
    public ResponseEntity<?> shortenUrl(@RequestBody ShortenUrlRequest request) {
        URL url;
        try {
            url = new URL(request.getUrl());
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body("Malformed URL provided");
        }
        return new ResponseEntity<>(buildShortenUrlResponse(urlHashService.storeUrlHash(url, hashGenerator.generate())), HttpStatus.OK);
    }

    private ShortenUrlResponse buildShortenUrlResponse(UrlHash urlHash) {
        return new ShortenUrlResponse(urlHash.getUrl().toString(),
                shortUrlService.buildShortUrl(urlHash.getHash()).toString());
    }

    @GetMapping("/url")
    public List<ShortenUrlResponse> getAllUrls() {
        return urlHashService.getAllUrls()
                .stream()
                .map(this::buildShortenUrlResponse)
                .collect(Collectors.toList());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception e) {
        log.error("URL shortener failed", e);
        return ResponseEntity.internalServerError().build();
    }
}
