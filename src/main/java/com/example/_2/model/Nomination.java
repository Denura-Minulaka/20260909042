package com.example._2.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "nominations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_training_officer",
            columnNames = {"training_programme_id", "officer_id"}
        )
    }
)
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_programme_id", nullable = false)
    private TrainingProgramme trainingProgramme;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "officer_id", nullable = false)
    private User officer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nominating_department_id", nullable = false)
    private Department nominatingDepartment;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    private LocalDateTime nominatedAt = LocalDateTime.now();

    public Nomination() {
    }

    public Nomination(Long id, TrainingProgramme trainingProgramme, User officer, Department nominatingDepartment, String status, LocalDateTime nominatedAt) {
        this.id = id;
        this.trainingProgramme = trainingProgramme;
        this.officer = officer;
        this.nominatingDepartment = nominatingDepartment;
        this.status = status;
        this.nominatedAt = nominatedAt;
    }

    public Nomination(TrainingProgramme trainingProgramme, User officer, Department nominatingDepartment, String status) {
        this.trainingProgramme = trainingProgramme;
        this.officer = officer;
        this.nominatingDepartment = nominatingDepartment;
        this.status = status;
        this.nominatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingProgramme getTrainingProgramme() {
        return trainingProgramme;
    }

    public void setTrainingProgramme(TrainingProgramme trainingProgramme) {
        this.trainingProgramme = trainingProgramme;
    }

    public User getOfficer() {
        return officer;
    }

    public void setOfficer(User officer) {
        this.officer = officer;
    }

    public Department getNominatingDepartment() {
        return nominatingDepartment;
    }

    public void setNominatingDepartment(Department nominatingDepartment) {
        this.nominatingDepartment = nominatingDepartment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getNominatedAt() {
        return nominatedAt;
    }

    public void setNominatedAt(LocalDateTime nominatedAt) {
        this.nominatedAt = nominatedAt;
    }
}
