package com.piral.liveweatherapp.dto;

import java.util.Date;

public class WeatherMessageDto {

    private String countryCode;
    private String city;
    private Float temp;
    private Float feelTemp;
    private String date ;
    private String description;
    private Character unitChar;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getFeelTemp() {
        return feelTemp;
    }

    public void setFeelTemp(Float feelTemp) {
        this.feelTemp = feelTemp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Character getUnitChar() {
        return unitChar;
    }

    public void setUnitChar(Character unitChar) {
        this.unitChar = unitChar;
    }
}
