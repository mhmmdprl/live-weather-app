package com.piral.liveweatherapp.enums;

import java.util.HashMap;
import java.util.Map;

public enum SettingsEnum {

    LANGUAGE ("lang"),
    UNITS("units"),
    CITY("city");

    private static final Map<String, SettingsEnum> lookup = new HashMap<String, SettingsEnum>();

    static {
        for (SettingsEnum d : SettingsEnum.values()) {
            lookup.put(d.getSetting(), d);
        }
    }

    final String setting;

    SettingsEnum(String setting) {
        this.setting = setting;
    }

    public String getSetting() {
        return setting;
    }

    public static SettingsEnum get(String setting) {
        return lookup.get(setting);
    }
}
