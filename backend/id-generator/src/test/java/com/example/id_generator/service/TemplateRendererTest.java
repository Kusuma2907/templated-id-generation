package com.example.id_generator.service;

import com.example.id_generator.service.ConfigurationService;
import com.example.id_generator.service.CounterService;
import com.example.id_generator.service.TemplateRenderer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplateRendererTest {

    @Test
    void shouldRenderFiscalYear() {

        CounterService counterService =
                Mockito.mock(CounterService.class);

        ConfigurationService configurationService =
                new ConfigurationService();

        TemplateRenderer renderer =
                new TemplateRenderer(
                        counterService,
                        configurationService
                );

        ConfigurationService.TemplateConfig template =
                new ConfigurationService.TemplateConfig(
                        "PG_RECEIPT",
                        "Payment receipt",
                        "PG-[CITY]-[FY:yyyy-yy]-[SEQ:6]"
                );

        ConfigurationService.CityConfig city =
                new ConfigurationService.CityConfig(
                        "TENANT1",
                        "AMR",
                        "Amaravati"
                );

        Mockito.when(
                counterService.nextValue(
                        "SEQ:PG_RECEIPT",
                        "AMR"
                )
        ).thenReturn(431L);

        String result =
                renderer.render(
                        template,
                        city,
                        LocalDate.of(2026, 11, 5)
                );

        assertEquals(
                "PG-AMR-2026-27-000431",
                result
        );
    }

    @Test
    void shouldRenderCalendarYear() {

        CounterService counterService =
                Mockito.mock(CounterService.class);

        ConfigurationService configurationService =
                new ConfigurationService();

        TemplateRenderer renderer =
                new TemplateRenderer(
                        counterService,
                        configurationService
                );

        ConfigurationService.TemplateConfig template =
                new ConfigurationService.TemplateConfig(
                        "TL_LICENSE",
                        "Trade license",
                        "TL/[CITY]/[CY:yyyy]/[SEQ:5]"
                );

        ConfigurationService.CityConfig city =
                new ConfigurationService.CityConfig(
                        "TENANT1",
                        "AMR",
                        "Amaravati"
                );

        Mockito.when(
                counterService.nextValue(
                        "SEQ:TL_LICENSE",
                        "AMR"
                )
        ).thenReturn(7L);

        String result =
                renderer.render(
                        template,
                        city,
                        LocalDate.of(2026, 11, 5)
                );

        assertEquals(
                "TL/AMR/2026/00007",
                result
        );
    }
}