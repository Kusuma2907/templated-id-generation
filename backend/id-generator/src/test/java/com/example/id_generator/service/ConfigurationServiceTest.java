package com.example.id_generator.service;

import com.example.id_generator.service.ConfigurationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationServiceTest {

    @Test
    void shouldResolveCityByTenantId() {

        ConfigurationService service =
                new ConfigurationService();

        service.addCity(
                "TENANT001",
                "AMR",
                "Amaravati"
        );

        ConfigurationService.CityConfig city =
                service.resolveCity("TENANT001");

        assertEquals("AMR", city.cityCode());
    }

    @Test
    void shouldResolveCityByCityCode() {

        ConfigurationService service =
                new ConfigurationService();

        service.addCity(
                "TENANT001",
                "AMR",
                "Amaravati"
        );

        ConfigurationService.CityConfig city =
                service.resolveCity("AMR");

        assertEquals("TENANT001", city.tenantId());
    }

    @Test
    void shouldRejectUnknownTemplate() {

        ConfigurationService service =
                new ConfigurationService();

        assertThrows(
                RuntimeException.class,
                () -> service.getTemplate("UNKNOWN")
        );
    }
}