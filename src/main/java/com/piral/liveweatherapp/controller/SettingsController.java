package com.piral.liveweatherapp.controller;

import com.piral.liveweatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public Object getSettingsList() {
        return this.weatherService.getSettingsAndList();
    }

}
