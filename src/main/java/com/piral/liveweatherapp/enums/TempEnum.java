package com.piral.liveweatherapp.enums;

public enum TempEnum {

    KELVIN("standard"),
    FAHRENHEIT("imperial"),
    CELSIUS("metric");

    final String tempType;


    TempEnum(String tempType) {
        this.tempType = tempType;
    }

    public String getTempType() {
        return tempType;
    }
}
