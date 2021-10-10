package com.url_shortener.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class JdbcConfiguration extends AbstractJdbcConfiguration {

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        List<Converter> converters = new ArrayList<>();
        converters.add(StringToUrlConverter.INSTANCE);
        converters.add(UrlToStringConverter.INSTANCE);
        return new JdbcCustomConversions(converters);
    }

    @ReadingConverter
    enum StringToUrlConverter implements Converter<String, URL> {
        INSTANCE;
        @SneakyThrows
        @Override
        public URL convert(String source) {
            return new URL(source);
        }
    }

    @WritingConverter
    enum UrlToStringConverter implements Converter<URL, String> {
        INSTANCE;
        @SneakyThrows
        @Override
        public String convert(URL source) {
            return source.toString();
        }
    }
}