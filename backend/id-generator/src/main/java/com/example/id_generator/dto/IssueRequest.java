package com.example.id_generator.dto;

import jakarta.validation.constraints.NotBlank;

public record IssueRequest(

        @NotBlank(message = "templateId is required")
        String templateId,

        @NotBlank(message = "city is required")
        String city
) {
}