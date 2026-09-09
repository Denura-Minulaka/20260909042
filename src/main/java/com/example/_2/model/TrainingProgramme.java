package com.example._2.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "training_programmes")
public class TrainingProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDate trainingDate;

    @Column(nullable = false)
    private Integer maxParticipants;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "training_target_departments",
        joinColumns = @JoinColumn(name = "training_id"),
        inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> targetDepartments = new HashSet<>();

    private String status = "PUBLISHED"; // PUBLISHED, COMPLETED, CANCELLED

    // TASK 3: Eligibility requirements
    private Integer minYearsOfService; // e.g., 2 years

    private String requiredGrade; // e.g., "Grade I"

    public TrainingProgramme() {
    }

    public TrainingProgramme(Long id, String title, String description, LocalDate trainingDate, Integer maxParticipants, Venue venue, Trainer trainer, Set<Department> targetDepartments, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.trainingDate = trainingDate;
        this.maxParticipants = maxParticipants;
        this.venue = venue;
        this.trainer = trainer;
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

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Set<Department> getTargetDepartments() {
        return targetDepartments;
    }

    public void setTargetDepartments(Set<Department> targetDepartments) {
        this.targetDepartments = targetDepartments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
