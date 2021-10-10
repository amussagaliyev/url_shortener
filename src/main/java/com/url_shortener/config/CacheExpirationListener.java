package com.url_shortener.config;

import com.url_shortener.repository.UrlHashRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CacheExpirationListener implements MessageListener {

    private final UrlHashRepository urlHashRepository;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String key = new String(message.getBody()).replace("urlHashes::", "");
        log.debug("Key expired {}", key);
        urlHashRepository.deleteByHash(key);
    }
}