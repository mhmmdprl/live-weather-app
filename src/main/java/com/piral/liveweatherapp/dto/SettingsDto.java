package com.piral.liveweatherapp.dto;

import java.util.Map;

public class SettingsDto {

    private JsonPropertiesObject jsonPropertiesObject;
    private  Map<String, Object> properties;

    public JsonPropertiesObject getJsonPropertiesObject() {
        return jsonPropertiesObject;
    }

    public void setJsonPropertiesObject(JsonPropertiesObject jsonPropertiesObject) {
        this.jsonPropertiesObject = jsonPropertiesObject;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
