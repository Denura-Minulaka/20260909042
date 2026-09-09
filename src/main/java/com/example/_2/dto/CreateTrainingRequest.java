package com.example._2.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public class CreateTrainingRequest {

    @NotBlank(message = "Training title is mandatory")
    private String title;

    private String description;

    @NotNull(message = "Training date is mandatory")
    @FutureOrPresent(message = "Training date must be today or in the future")
    private LocalDate trainingDate;

    @NotNull(message = "Maximum participants is mandatory")
    @Min(value = 1, message = "Maximum participants must be at least 1")
    private Integer maxParticipants;

    @NotNull(message = "Venue ID is mandatory")
    private Long venueId;

    @NotNull(message = "Trainer ID is mandatory")
    private Long trainerId;

    @NotEmpty(message = "Target department IDs are mandatory")
    private Set<Long> targetDepartmentIds;

    // TASK 3: Optional Eligibility fields
    private Integer minYearsOfService;

    private String requiredGrade;

    public CreateTrainingRequest() {
    }

    public CreateTrainingRequest(String title, String description, LocalDate trainingDate, Integer maxParticipants, Long venueId, Long trainerId, Set<Long> targetDepartmentIds) {
        this.title = title;
        this.description = description;
        this.trainingDate = trainingDate;
        this.maxParticipants = maxParticipants;
        this.venueId = venueId;
        this.trainerId = trainerId;
        this.targetDepartmentIds = targetDepartmentIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public Set<Long> getTargetDepartmentIds() {
        return targetDepartmentIds;
    }

    public void setTargetDepartmentIds(Set<Long> targetDepartmentIds) {
        this.targetDepartmentIds = targetDepartmentIds;
    }

    public Integer getMinYearsOfService() {
        return minYearsOfService;
    }

    public void setMinYearsOfService(Integer minYearsOfService) {
        this.minYearsOfService = minYearsOfService;
    }

    public String getRequiredGrade() {
        return requiredGrade;
    }

    public void setRequiredGrade(String requiredGrade) {
        this.requiredGrade = requiredGrade;
    }
}
