package com.example.id_generator.service;

import com.example.id_generator.entity.CounterEntity;
import com.example.id_generator.repository.CounterRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CounterService {

    private static final Pattern PLACEHOLDER =
            Pattern.compile(
                    "\\[([A-Z]+(?:_[A-Z]+)*):([^\\]]+)\\]"
            );

    private final CounterRepository counterRepository;
    private final ConfigurationService configurationService;

    public CounterService(
            CounterRepository counterRepository,
            ConfigurationService configurationService) {

        this.counterRepository = counterRepository;
        this.configurationService = configurationService;
    }

    public void initializeCounters() {

        for (ConfigurationService.TemplateConfig template :
                configurationService.getTemplates()) {

            for (ConfigurationService.CityConfig city :
                    configurationService.getCities()) {

                initializeForTemplate(
                        template,
                        city.cityCode()
                );
            }
        }
    }

    private void initializeForTemplate(
            ConfigurationService.TemplateConfig template,
            String cityCode) {

        Matcher matcher =
                PLACEHOLDER.matcher(template.format());

        while (matcher.find()) {

            String type = matcher.group(1);

            if ("SEQ".equals(type)) {

                createIfMissing(
                        "SEQ:" + template.id(),
                        cityCode,
                        1
                );
} else if (type.startsWith("SEQ_")) {

    String counterName = type;

    if (configurationService
            .hasNamedCounter(counterName)) {

        createIfMissing(
                "NAMED:" + counterName,
                cityCode,
                configurationService
                        .getNamedCounterStart(
                                counterName
                        )
        );
    }

                } 
        }
    }

    private void createIfMissing(
            String counterKey,
            String scopeKey,
            long start) {

        if (!counterRepository
                .existsByCounterKeyAndScopeKey(
                        counterKey,
                        scopeKey)) {

            counterRepository.save(
                    new CounterEntity(
                            counterKey,
                            scopeKey,
                            start
                    )
            );
        }
    }

    public long nextValue(
            String counterKey,
            String scopeKey) {

        CounterEntity counter =
                counterRepository
                        .findForUpdate(
                                counterKey,
                                scopeKey
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Counter not initialized: "
                                                + counterKey
                                                + "/"
                                                + scopeKey
                                )
                        );

        long current =
                counter.getNextValue();

        counter.setNextValue(current + 1);

        return current;
    }
}