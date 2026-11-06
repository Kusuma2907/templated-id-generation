package com.example.id_generator.service;

import com.example.id_generator.dto.BulkIssueResponse;
import com.example.id_generator.dto.IssueResponse;
import com.example.id_generator.entity.IssuedIdEntity;
import com.example.id_generator.repository.IssuedIdRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class IdGenerationService {

    private final ConfigurationService configurationService;
    private final TemplateRenderer templateRenderer;
    private final IssuedIdRepository issuedIdRepository;

    public IdGenerationService(
            ConfigurationService configurationService,
            TemplateRenderer templateRenderer,
            IssuedIdRepository issuedIdRepository) {

        this.configurationService = configurationService;
        this.templateRenderer = templateRenderer;
        this.issuedIdRepository = issuedIdRepository;
    }

    @Transactional
    public IssueResponse issue(
            String templateId,
            String cityValue) {

        ConfigurationService.TemplateConfig template =
                configurationService.getTemplate(templateId);

        ConfigurationService.CityConfig city =
                configurationService.resolveCity(cityValue);

        templateRenderer.validate(template);

        String generatedId =
                templateRenderer.render(
                        template,
                        city,
                        LocalDate.now()
                );

        issuedIdRepository.save(
                new IssuedIdEntity(
                        template.id(),
                        city.cityCode(),
                        generatedId
                )
        );

        return new IssueResponse(
                generatedId,
                template.id(),
                city.cityCode()
        );
    }

    @Transactional
    public BulkIssueResponse issueBulk(
            String templateId,
            String cityValue,
            int count) {

        ConfigurationService.TemplateConfig template =
                configurationService.getTemplate(templateId);

        ConfigurationService.CityConfig city =
                configurationService.resolveCity(cityValue);

        /*
         * Validate before consuming any sequence value.
         * This is especially important for a bulk request.
         */
        templateRenderer.validate(template);

        List<String> ids =
                new ArrayList<>(count);

        LocalDate date =
                LocalDate.now();

        for (int i = 0; i < count; i++) {

            String generatedId =
                    templateRenderer.render(
                            template,
                            city,
                            date
                    );

            issuedIdRepository.save(
                    new IssuedIdEntity(
                            template.id(),
                            city.cityCode(),
                            generatedId
                    )
            );

            ids.add(generatedId);
        }

        return new BulkIssueResponse(
                template.id(),
                city.cityCode(),
                count,
                ids
        );
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> recent(int limit) {

        List<IssuedIdEntity> entities =
                issuedIdRepository
                        .findAllByOrderByCreatedAtDesc(
                                PageRequest.of(0, limit)
                        );

        return entities.stream()
                .map(entity ->
                        new IssueResponse(
                                entity.getGeneratedId(),
                                entity.getTemplateId(),
                                entity.getCityCode()
                        )
                )
                .toList();
    }
}