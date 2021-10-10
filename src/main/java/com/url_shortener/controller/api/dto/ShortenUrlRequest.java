package com.url_shortener.controller.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenUrlRequest {
    private String url;
}
