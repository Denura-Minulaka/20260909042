package com.example._2.controller;

import com.example._2.dto.ApiResponse;
import com.example._2.dto.CreateTrainingRequest;
import com.example._2.dto.TrainingResponse;
import com.example._2.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainings")
@Tag(name = "Training Programmes", description = "Endpoints for managing training programmes")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_COORDINATOR')")
    @Operation(summary = "Create Training Programme", description = "Creates a new training programme (Coordinator only)")
    public ResponseEntity<ApiResponse<TrainingResponse>> createTraining(@Valid @RequestBody CreateTrainingRequest request) {
        TrainingResponse response = trainingService.createTraining(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Training programme created successfully", response));
    }

    @GetMapping
    @Operation(summary = "List All Training Programmes", description = "Retrieves all published training programmes")
    public ResponseEntity<ApiResponse<List<TrainingResponse>>> getAllTrainings() {
        List<TrainingResponse> responses = trainingService.getAllTrainings();
        return ResponseEntity.ok(ApiResponse.success("Training programmes retrieved successfully", responses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Training Programme Details", description = "Retrieves details of a specific training programme")
    public ResponseEntity<ApiResponse<TrainingResponse>> getTrainingById(@PathVariable Long id) {
        TrainingResponse response = trainingService.getTrainingById(id);
        return ResponseEntity.ok(ApiResponse.success("Training programme details retrieved successfully", response));
    }
}
