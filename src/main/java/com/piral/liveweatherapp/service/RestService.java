package com.piral.liveweatherapp.service;

import com.google.gson.Gson;
import com.piral.liveweatherapp.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public abstract class RestService {

    @Autowired
    private RestTemplate template;

    public <T> T getObject(String url, HttpHeaders headers, Class<T> type) {
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response;
        try {
            response = template.exchange(url, HttpMethod.GET, entity, String.class);
            return new Gson().fromJson(response.getBody(), type);
        } catch (HttpStatusCodeException e) {
            throw new CustomException(e.getResponseBodyAsString(), HttpStatus.valueOf(e.getRawStatusCode()));
        } catch (Exception e) {
            throw e;
        }
    }
}
