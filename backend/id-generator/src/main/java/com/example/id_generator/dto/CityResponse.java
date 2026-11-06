package com.example.id_generator.dto;

public record CityResponse(
        String tenantId,
        String cityCode,
        String cityName
) {
}