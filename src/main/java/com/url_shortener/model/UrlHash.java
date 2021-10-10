package com.url_shortener.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.net.URL;

@Getter
@Setter
@Accessors(chain = true)
@Table
public class UrlHash implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;
    @Id
    private Integer id;
    private URL url;
    private String hash;
}
