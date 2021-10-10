package com.url_shortener.controller.page;

import com.url_shortener.model.UrlHash;
import com.url_shortener.service.UrlHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ShortUrlController {

    public final UrlHashService urlHashService;

    @GetMapping("/s/{hash}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String hash) {
            UrlHash originalUrl = urlHashService.getOriginalUrlByHash(hash);
            if (originalUrl == null) {
                return ResponseEntity
                        .status(HttpStatus.MOVED_TEMPORARILY)
                        .header("Location", "could_not_redirect.html")
                        .build();
            }
            return ResponseEntity
                    .status(HttpStatus.MOVED_TEMPORARILY)
                    .header("Location", originalUrl.getUrl().toString())
                    .build();
    }
}
