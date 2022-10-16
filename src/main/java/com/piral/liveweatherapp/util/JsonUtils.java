package com.piral.liveweatherapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.piral.liveweatherapp.dto.JsonPropertiesObject;
import com.piral.liveweatherapp.exception.CustomException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JsonUtils {
    private static JsonPropertiesObject jsonPropertiesObject = null;

    public JsonPropertiesObject getPropertiesJson() {
        if (jsonPropertiesObject == null) {
            InputStream input = null;
            try {
                ClassPathResource staticDataResource = new ClassPathResource("properties.json");
                input = this.getClass().getClassLoader().getResourceAsStream(staticDataResource.getPath());
                ObjectMapper mapper = new ObjectMapper();

                jsonPropertiesObject = mapper.readValue(new InputStreamReader(input, StandardCharsets.UTF_8), JsonPropertiesObject.class);
            } catch (Exception e) {
               throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return jsonPropertiesObject;
    }
}
