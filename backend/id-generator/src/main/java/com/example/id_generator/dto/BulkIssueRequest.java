package com.example.id_generator.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BulkIssueRequest(

        @NotBlank(message = "templateId is required")
        String templateId,

        @NotBlank(message = "city is required")
        String city,

        @Min(
                value = 1,
                message = "count must be at least 1"
        )
        @Max(
                value = 1000,
                message = "count must not exceed 1000"
        )
        int count
) {
}