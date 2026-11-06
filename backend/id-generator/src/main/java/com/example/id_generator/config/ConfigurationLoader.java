package com.example.id_generator.config;

import com.example.id_generator.service.ConfigurationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.example.id_generator.service.CounterService;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class ConfigurationLoader implements CommandLineRunner {

    private final ConfigurationService configurationService;
private final CounterService counterService;
private final ObjectMapper objectMapper;

    @Value("${id-generator.data-directory:data}")
    private String dataDirectory;

    public ConfigurationLoader(
        ConfigurationService configurationService,
        CounterService counterService,
        ObjectMapper objectMapper) {

    this.configurationService = configurationService;
    this.counterService = counterService;
    this.objectMapper = objectMapper;
}

    @Override
    public void run(String... args) throws Exception {
        loadTemplates();
        loadCities();
        counterService.initializeCounters();
    }

    private void loadTemplates() throws Exception {

        ClassPathResource resource =
                new ClassPathResource(dataDirectory + "/templates.json");

        if (!resource.exists()) {
            throw new IllegalStateException(
                    "Template configuration not found: "
                            + dataDirectory + "/templates.json"
            );
        }

        JsonNode root =
                objectMapper.readTree(resource.getInputStream());

        JsonNode templates = root.get("templates");

        if (templates == null || !templates.isArray()) {
            throw new IllegalStateException(
                    "Invalid templates.json: 'templates' array is missing"
            );
        }

        for (JsonNode template : templates) {

            String id = template.get("id").asText();
            String name = template.get("name").asText();
            String format = template.get("format").asText();

            configurationService.addTemplate(
                    id,
                    name,
                    format
            );
        }

        JsonNode counters = root.get("counters");

if (counters != null && counters.isObject()) {

    for (String counterName : counters.propertyNames()) {

        JsonNode counterConfig = counters.get(counterName);

        JsonNode startNode = counterConfig.get("start");

        if (startNode == null) {
            throw new IllegalStateException(
                    "Missing 'start' for counter: " + counterName
            );
        }

        configurationService.addNamedCounter(
                counterName,
                startNode.asLong()
        );
    }
}
    }

    private void loadCities() throws Exception {

        ClassPathResource resource =
                new ClassPathResource(dataDirectory + "/cities.csv");

        if (!resource.exists()) {
            throw new IllegalStateException(
                    "City configuration not found: "
                            + dataDirectory + "/cities.csv"
            );
        }

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        resource.getInputStream(),
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            String header = reader.readLine();

            if (header == null) {
                throw new IllegalStateException(
                        "cities.csv is empty"
                );
            }

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                if (parts.length < 3) {
                    throw new IllegalStateException(
                            "Invalid cities.csv row: " + line
                    );
                }

                String tenantId = parts[0].trim();
                String cityCode = parts[1].trim();
                String cityName = parts[2].trim();

                configurationService.addCity(
                        tenantId,
                        cityCode,
                        cityName
                );
            }
        }
    }
}