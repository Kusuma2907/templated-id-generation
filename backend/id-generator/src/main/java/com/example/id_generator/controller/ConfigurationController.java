package com.example.id_generator.controller;

import com.example.id_generator.dto.CityResponse;
import com.example.id_generator.dto.TemplateResponse;
import com.example.id_generator.service.ConfigurationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(
            ConfigurationService configurationService) {

        this.configurationService =
                configurationService;
    }

    @GetMapping("/templates")
    public List<TemplateResponse> templates() {

        return configurationService
                .getTemplates()
                .stream()
                .map(template ->
                        new TemplateResponse(
                                template.id(),
                                template.name(),
                                template.format()
                        )
                )
                .toList();
    }

    @GetMapping("/cities")
    public List<CityResponse> cities() {

        return configurationService
                .getCities()
                .stream()
                .map(city ->
                        new CityResponse(
                                city.tenantId(),
                                city.cityCode(),
                                city.cityName()
                        )
                )
                .toList();
    }
}