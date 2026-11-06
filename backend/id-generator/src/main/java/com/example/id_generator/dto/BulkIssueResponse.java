package com.example.id_generator.dto;

import java.util.List;

public record BulkIssueResponse(
        String templateId,
        String cityCode,
        int count,
        List<String> ids
) {
}