package com.example.id_generator.service;

import com.example.id_generator.exception.MissingCounterConfigurationException;
import com.example.id_generator.exception.UnknownCityException;
import com.example.id_generator.exception.UnknownTemplateException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationService {

    private final Map<String, TemplateConfig> templates =
            new LinkedHashMap<>();

    private final Map<String, CityConfig> citiesByTenantId =
            new LinkedHashMap<>();

    private final Map<String, CityConfig> citiesByCode =
            new LinkedHashMap<>();

    private final Map<String, Long> namedCounters =
            new LinkedHashMap<>();

    public void addTemplate(
            String id,
            String name,
            String format) {

        templates.put(
                id,
                new TemplateConfig(id, name, format)
        );
    }

    public void addCity(
            String tenantId,
            String cityCode,
            String cityName) {

        CityConfig city =
                new CityConfig(
                        tenantId,
                        cityCode,
                        cityName
                );

        citiesByTenantId.put(tenantId, city);
        citiesByCode.put(cityCode, city);
    }

    public void addNamedCounter(
            String name,
            long start) {

        namedCounters.put(name, start);
    }

    public TemplateConfig getTemplate(String templateId) {

        TemplateConfig template =
                templates.get(templateId);

        if (template == null) {
            throw new UnknownTemplateException(
                    "Unknown template: " + templateId
            );
        }

        return template;
    }

    public CityConfig resolveCity(String value) {

        CityConfig city =
                citiesByTenantId.get(value);

        if (city == null) {
            city = citiesByCode.get(value);
        }

        if (city == null) {
            throw new UnknownCityException(
                    "Unknown city or tenant: " + value
            );
        }

        return city;
    }

    public long getNamedCounterStart(String name) {

        Long start = namedCounters.get(name);

        if (start == null) {
            throw new MissingCounterConfigurationException(
                    "No configuration found for named counter: "
                            + name
            );
        }

        return start;
    }

    public boolean hasNamedCounter(String name) {
        return namedCounters.containsKey(name);
    }

    public List<TemplateConfig> getTemplates() {
        return new ArrayList<>(templates.values());
    }

    public List<CityConfig> getCities() {
        return new ArrayList<>(citiesByCode.values());
    }

    public record TemplateConfig(
            String id,
            String name,
            String format
    ) {
    }

    public record CityConfig(
            String tenantId,
            String cityCode,
            String cityName
    ) {
    }
}