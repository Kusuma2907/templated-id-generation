package com.example.id_generator.dto;

public record IssueResponse(
        String id,
        String templateId,
        String cityCode
) {
}