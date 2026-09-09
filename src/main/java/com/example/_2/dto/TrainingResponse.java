package com.example._2.dto;

import java.time.LocalDate;
import java.util.List;

public class TrainingResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate trainingDate;
    private Integer maxParticipants;
    private Long currentNominationCount;
    private String venueName;
    private Integer venueCapacity;
    private String trainerName;
    private String trainerType;
    private List<String> targetDepartments;
    private String status;

    public TrainingResponse() {
    }

    public TrainingResponse(Long id, String title, String description, LocalDate trainingDate, Integer maxParticipants, Long currentNominationCount, String venueName, Integer venueCapacity, String trainerName, String trainerType, List<String> targetDepartments, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.trainingDate = trainingDate;
        this.maxParticipants = maxParticipants;
        this.currentNominationCount = currentNominationCount;
        this.venueName = venueName;
        this.venueCapacity = venueCapacity;
        this.trainerName = trainerName;
        this.trainerType = trainerType;
        this.targetDepartments = targetDepartments;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCurrentNominationCount() {
        return currentNominationCount;
    }

    public void setCurrentNominationCount(Long currentNominationCount) {
        this.currentNominationCount = currentNominationCount;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Integer getVenueCapacity() {
        return venueCapacity;
    }

    public void setVenueCapacity(Integer venueCapacity) {
        this.venueCapacity = venueCapacity;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrainerType() {
        return trainerType;
    }

    public void setTrainerType(String trainerType) {
        this.trainerType = trainerType;
    }

    public List<String> getTargetDepartments() {
        return targetDepartments;
    }

    public void setTargetDepartments(List<String> targetDepartments) {
        this.targetDepartments = targetDepartments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
