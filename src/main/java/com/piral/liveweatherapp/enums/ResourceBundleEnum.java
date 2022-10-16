package com.piral.liveweatherapp.enums;

public enum ResourceBundleEnum {
    UNEXPECTED_ERROR("UnexpectedError"),
    NOT_FOUND_CITY("NotFoundCity"),
    NOT_FOUND_LANG("NotFoundLang"),
    NOT_FOUND_UNITS("NotFoundUnits");

    private final String value;

    private ResourceBundleEnum() {
        this.value = null;
    }
    ResourceBundleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
