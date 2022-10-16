package com.piral.liveweatherapp.service;

import com.google.gson.JsonObject;
import com.piral.liveweatherapp.dto.JsonPropertiesObject;
import com.piral.liveweatherapp.dto.WeatherMessageDto;
import com.piral.liveweatherapp.enums.ResourceBundleEnum;
import com.piral.liveweatherapp.enums.SettingsEnum;
import com.piral.liveweatherapp.enums.TempEnum;
import com.piral.liveweatherapp.exception.CustomException;
import com.piral.liveweatherapp.util.DateUtils;
import com.piral.liveweatherapp.util.JsonUtils;
import com.piral.liveweatherapp.util.UrlBuilder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class WeatherService extends RestService {

    @Value("${open.weather.map.url}")
    private String apiUrl;

    @Value("${open.weather.map.app.id}")
    private String appId;

    private final SimpMessagingTemplate messagingTemplate;
    private final JsonUtils jsonUtil;

    private static Map<String, Map<String, Object>> userLastSettingsMap = new HashMap<>();


    @Autowired
    public WeatherService(SimpMessagingTemplate messagingTemplate, JsonUtils jsonUtil) {
        this.messagingTemplate = messagingTemplate;
        this.jsonUtil = jsonUtil;
    }

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    public Set<SimpUser> getUsers() {
        return simpUserRegistry.getUsers();
    }

    @Scheduled(fixedDelay = 10000)
    public void sendMessage() {
        Set<SimpUser> simpUsers = getUsers();
        if (simpUsers != null) {
            simpUsers.forEach(user -> {
                WeatherMessageDto weatherMessageDto = getMessage(user.getName());
                if (weatherMessageDto != null) {
                    messagingTemplate.convertAndSendToUser(user.getName(), "/user/topic/user-messages", weatherMessageDto);
                }
            });
        }
    }

    @Scheduled(fixedDelay = 50000)
    public void removeCache() {
        Set<SimpUser> simpUsers = getUsers();

        if (simpUsers != null) {

            Set<String> userIds = simpUsers.stream().map(SimpUser::getName).collect(Collectors.toSet());
            Set<String> removedIdList = new HashSet<>();
            for (Map.Entry<String, Map<String, Object>> entry : userLastSettingsMap.entrySet()) {
                if (!userIds.contains(entry.getKey())) {
                    removedIdList.add(entry.getKey());
                }
            }
            removedIdList.forEach(id -> {
                userLastSettingsMap.remove(id);
            });
        }
    }


    public WeatherMessageDto getMessage(String id) {
        JsonObject jsonResponse;
        if (id == null) {
            jsonResponse = getObject(createUrl(), new HttpHeaders(), JsonObject.class);
        } else {
            jsonResponse = getObject(createUrlForUser(id), new HttpHeaders(), JsonObject.class);
        }
        WeatherMessageDto weatherMessageDto = new WeatherMessageDto();
        weatherMessageDto.setCity(jsonResponse.get("name").getAsString());
        weatherMessageDto.setCountryCode(jsonResponse.getAsJsonObject("sys").get("country").getAsString());
        weatherMessageDto.setTemp(jsonResponse.getAsJsonObject("main").get("temp").getAsFloat());
        weatherMessageDto.setFeelTemp(jsonResponse.getAsJsonObject("main").get("feels_like").getAsFloat());
        weatherMessageDto.setDescription(jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString());
        weatherMessageDto.setDate(DateUtils.dateFormat(new Date()));
        weatherMessageDto.setUnitChar(this.getUnitChar(id));
        return weatherMessageDto;
    }

    private Character getUnitChar(String id) {
        if (id == null) {
            return 'C';
        }
        switch (userLastSettingsMap.get(id).get("units").toString().toLowerCase(Locale.US)) {
            case "metric":
                return 'C';
            case "imperial":
                return 'F';
            default:
                return 'K';
        }
    }

    public String createUrlForUser(String id) {
        Map<String, Object> queryMap = userLastSettingsMap.get(id);
        UrlBuilder builder = new UrlBuilder(apiUrl);
        return builder
            .queryIfNotEmpty("q", queryMap.get("city"))
            .queryIfNotEmpty("lang", queryMap.get("lang"))
            .queryIfNotEmpty("units", queryMap.get("units"))
            .queryIfNotEmpty("appid", appId).build();
    }

    public String createUrl() {
        UrlBuilder builder = new UrlBuilder(apiUrl);
        return builder
            .queryIfNotEmpty("q", "Adana")
            .queryIfNotEmpty("lang", "tr")
            .queryIfNotEmpty("units", TempEnum.CELSIUS.getTempType())
            .queryIfNotEmpty("appid", appId).build();
    }

    public WeatherMessageDto setProperties(SettingsEnum settingsEnum, Object value, String id) {
        if (value != null) {
            JsonPropertiesObject jsonPropertiesObject = this.jsonUtil.getPropertiesJson();

            switch (settingsEnum) {
                case CITY:
                    if (!jsonPropertiesObject.getCities().contains(value.toString().toUpperCase(Locale.ENGLISH))) {
                        throw new CustomException(ResourceBundleEnum.NOT_FOUND_CITY, HttpStatus.BAD_REQUEST, value.toString());
                    }
                    break;
                case UNITS:
                    if (!jsonPropertiesObject.getUnits().contains(value.toString().toUpperCase(Locale.ENGLISH))) {
                        throw new CustomException(ResourceBundleEnum.NOT_FOUND_UNITS, HttpStatus.BAD_REQUEST, value.toString());
                    }
                    break;
                case LANGUAGE:
                    if (!jsonPropertiesObject.getLanguages().contains(value.toString().toUpperCase(Locale.ENGLISH))) {
                        throw new CustomException(ResourceBundleEnum.NOT_FOUND_LANG, HttpStatus.BAD_REQUEST, value.toString());
                    }
                    break;
                default:
                    throw new CustomException(ResourceBundleEnum.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Map<String, Object> settingMap = userLastSettingsMap.get(id);
            settingMap.put(settingsEnum.getSetting(), value);
            userLastSettingsMap.put(id, settingMap);
            return getMessage(id);
        }
        return null;
    }

    public JsonPropertiesObject getSettingsAndList() {
        return this.jsonUtil.getPropertiesJson();
    }

    public static void setUserSettings(String id, Map<String, Object> map) {
        userLastSettingsMap.put(id, map);
    }
}
