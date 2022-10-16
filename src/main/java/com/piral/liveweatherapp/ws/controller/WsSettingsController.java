package com.piral.liveweatherapp.ws.controller;

import com.piral.liveweatherapp.dto.SettingMessageDto;
import com.piral.liveweatherapp.enums.SettingsEnum;
import com.piral.liveweatherapp.service.WeatherService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class WsSettingsController {

    @Autowired
    private WeatherService weatherService;

    @MessageMapping("/user-message")
    @SendToUser("/topic/user-messages")
    public Object getMessage(final SettingMessageDto message, final Principal principal) {
        return this.weatherService.setProperties(SettingsEnum.get(message.getKey()), message.getValue(), principal.getName());
    }
}
