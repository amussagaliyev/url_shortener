package com.url_shortener.service;

import com.url_shortener.model.UrlHash;
import com.url_shortener.repository.UrlHashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlHashService {
    private final UrlHashRepository urlHashRepository;

    @Cacheable(cacheNames = {"urlHashes"}, key = "#hash")
    public UrlHash storeUrlHash(URL url, String hash) {
        return urlHashRepository.save(new UrlHash().setUrl(url).setHash(hash));
    }

    @Cacheable(cacheNames = {"urlHashes"}, key = "#hash")
    public UrlHash getOriginalUrlByHash(String hash) {
        return urlHashRepository.getByHash(hash).orElse(null);
    }

    public List<UrlHash> getAllUrls() {
        return urlHashRepository.findAll();
    }

}
