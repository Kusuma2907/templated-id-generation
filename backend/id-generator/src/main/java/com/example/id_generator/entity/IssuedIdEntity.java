package com.example.id_generator.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "issued_id",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_issued_id_value",
                        columnNames = "generated_id"
                )
        },
        indexes = {
                @Index(
                        name = "idx_issued_created_at",
                        columnList = "created_at"
                )
        }
)
public class IssuedIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "template_id",
            nullable = false,
            length = 100
    )
    private String templateId;

    @Column(
            name = "city_code",
            nullable = false,
            length = 50
    )
    private String cityCode;

    @Column(
            name = "generated_id",
            nullable = false,
            length = 200
    )
    private String generatedId;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    protected IssuedIdEntity() {
    }

    public IssuedIdEntity(
            String templateId,
            String cityCode,
            String generatedId) {

        this.templateId = templateId;
        this.cityCode = cityCode;
        this.generatedId = generatedId;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}