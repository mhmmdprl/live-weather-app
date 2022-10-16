package com.piral.liveweatherapp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlBuilder {

    private String url;
    private Map<String, Object> queryParams = new HashMap<>();

    public UrlBuilder(String url) {
        this.url = url;
    }

    public UrlBuilder queryIfNotEmpty(String name, Object value) {
        if (value != null) {
            queryParams.put(name, value);
        }
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder(url);
        if (!queryParams.isEmpty()) {
            String queryString = queryParams.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
            sb.append("?").append(queryString);
        }
        return sb.toString();
    }
}
