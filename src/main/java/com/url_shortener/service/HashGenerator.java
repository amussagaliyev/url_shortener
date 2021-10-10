package com.url_shortener.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Service;

@Service
public class HashGenerator {
    public String generate() {
        return NanoIdUtils.randomNanoId();
    }
}
