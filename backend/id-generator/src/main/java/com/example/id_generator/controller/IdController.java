package com.example.id_generator.controller;

import com.example.id_generator.dto.BulkIssueRequest;
import com.example.id_generator.dto.BulkIssueResponse;
import com.example.id_generator.dto.IssueRequest;
import com.example.id_generator.dto.IssueResponse;
import com.example.id_generator.service.IdGenerationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ids")
public class IdController {

    private final IdGenerationService idGenerationService;

    public IdController(
            IdGenerationService idGenerationService) {

        this.idGenerationService =
                idGenerationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponse issue(
            @Valid @RequestBody IssueRequest request) {

        return idGenerationService.issue(
                request.templateId(),
                request.city()
        );
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public BulkIssueResponse issueBulk(
            @Valid @RequestBody BulkIssueRequest request) {

        return idGenerationService.issueBulk(
                request.templateId(),
                request.city(),
                request.count()
        );
    }

    @GetMapping("/recent")
    public List<IssueResponse> recent(
            @RequestParam(
                    defaultValue = "20"
            )
            int limit) {

        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException(
                    "limit must be between 1 and 100"
            );
        }

        return idGenerationService.recent(limit);
    }
}