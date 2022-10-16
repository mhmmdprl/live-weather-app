package com.piral.liveweatherapp.config;

import com.piral.liveweatherapp.enums.TempEnum;
import com.piral.liveweatherapp.service.WeatherService;
import com.sun.security.auth.UserPrincipal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final String randomId = UUID.randomUUID().toString();
        LOG.info("User with ID '{}' opened the page", randomId);
        Map<String, Object> lastSettingsMap = new HashMap<>();
        lastSettingsMap.put("city", "Adana");
        lastSettingsMap.put("lang", "tr");
        lastSettingsMap.put("units", TempEnum.CELSIUS.getTempType());
        WeatherService.setUserSettings(randomId, lastSettingsMap);
        return new UserPrincipal(randomId);
    }
}
