package com.example.id_generator.service;

import com.example.id_generator.exception.InvalidTemplateException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemplateRenderer {

    private static final Pattern PLACEHOLDER =
            Pattern.compile(
                    "\\[([A-Z]+(?:_[A-Z]+)*)(?::([^\\]]+))?\\]"
            );

    private final CounterService counterService;
    private final ConfigurationService configurationService;

    public TemplateRenderer(
            CounterService counterService,
            ConfigurationService configurationService) {

        this.counterService = counterService;
        this.configurationService = configurationService;
    }

    public void validate(
            ConfigurationService.TemplateConfig template) {

        Matcher matcher =
                PLACEHOLDER.matcher(template.format());

        while (matcher.find()) {

            String type = matcher.group(1);
            String argument = matcher.group(2);

            switch (type) {

                case "CITY":
                    break;

                case "FY":
                    validateFiscalFormat(
                            template.id(),
                            argument
                    );
                    break;

                case "CY":
                    validateDateFormat(
                            template.id(),
                            argument
                    );
                    break;

                case "SEQ":
                    validateWidth(
                            template.id(),
                            argument
                    );
                    break;

                default:

                    if (type.startsWith("SEQ_")) {

    String counterName = type;

    validateWidth(
            template.id(),
            argument
    );

    if (!configurationService
            .hasNamedCounter(counterName)) {

        throw new InvalidTemplateException(
                "Template "
                        + template.id()
                        + " references named counter "
                        + counterName
                        + " but no configuration exists"
        );
    }
} else {

                        throw new InvalidTemplateException(
                                "Unsupported placeholder ["
                                        + type
                                        + "] in template "
                                        + template.id()
                        );
                    }
            }
        }
    }

    public String render(
            ConfigurationService.TemplateConfig template,
            ConfigurationService.CityConfig city,
            LocalDate date) {

        validate(template);

        Matcher matcher =
                PLACEHOLDER.matcher(template.format());

        StringBuffer result =
                new StringBuffer();

        while (matcher.find()) {

            String type = matcher.group(1);
            String argument = matcher.group(2);

            String replacement =
                    resolve(
                            type,
                            argument,
                            template,
                            city,
                            date
                    );

            matcher.appendReplacement(
                    result,
                    Matcher.quoteReplacement(replacement)
            );
        }

        matcher.appendTail(result);

        return result.toString();
    }

    private String resolve(
            String type,
            String argument,
            ConfigurationService.TemplateConfig template,
            ConfigurationService.CityConfig city,
            LocalDate date) {

        return switch (type) {

            case "CITY" ->
                    city.cityCode();

            case "FY" ->
                    fiscalYear(date, argument);

            case "CY" ->
                    date.format(
                            DateTimeFormatter.ofPattern(
                                    argument,
                                    Locale.ROOT
                            )
                    );

            case "SEQ" -> {

                int width =
                        Integer.parseInt(argument);

                long value =
                        counterService.nextValue(
                                "SEQ:" + template.id(),
                                city.cityCode()
                        );

                yield zeroPad(value, width);
            }

            default -> {
if (type.startsWith("SEQ_")) {

    String counterName = type;

    int width =
            Integer.parseInt(argument);

    long value =
            counterService.nextValue(
                    "NAMED:" + counterName,
                    city.cityCode()
            );

    yield zeroPad(value, width);
}

                throw new InvalidTemplateException(
                        "Unsupported placeholder: " + type
                );
            }
        };
    }

    private String fiscalYear(
            LocalDate date,
            String format) {

        int startYear =
                date.getMonthValue() >= 4
                        ? date.getYear()
                        : date.getYear() - 1;

        int endYear =
                startYear + 1;

        return switch (format) {

            case "yyyy-yy" ->
                    String.format(
                            Locale.ROOT,
                            "%04d-%02d",
                            startYear,
                            endYear % 100
                    );

            case "yy-yy" ->
                    String.format(
                            Locale.ROOT,
                            "%02d-%02d",
                            startYear % 100,
                            endYear % 100
                    );

            default ->
                    throw new InvalidTemplateException(
                            "Unsupported fiscal year format: "
                                    + format
                    );
        };
    }

    private void validateFiscalFormat(
            String templateId,
            String format) {

        if (!"yyyy-yy".equals(format)
                && !"yy-yy".equals(format)) {

            throw new InvalidTemplateException(
                    "Unsupported FY format '"
                            + format
                            + "' in template "
                            + templateId
            );
        }
    }

    private void validateDateFormat(
            String templateId,
            String format) {

        if (format == null || format.isBlank()) {
            throw new InvalidTemplateException(
                    "Empty CY format in template "
                            + templateId
            );
        }

        try {
            DateTimeFormatter.ofPattern(
                    format,
                    Locale.ROOT
            );
        } catch (IllegalArgumentException exception) {
            throw new InvalidTemplateException(
                    "Invalid CY format '"
                            + format
                            + "' in template "
                            + templateId
            );
        }
    }

    private void validateWidth(
            String templateId,
            String widthText) {

        try {

            int width =
                    Integer.parseInt(widthText);

            if (width <= 0 || width > 18) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException exception) {

            throw new InvalidTemplateException(
                    "Invalid sequence width '"
                            + widthText
                            + "' in template "
                            + templateId
            );
        }
    }

    private String zeroPad(
            long value,
            int width) {

        return String.format(
                Locale.ROOT,
                "%0" + width + "d",
                value
        );
    }
}