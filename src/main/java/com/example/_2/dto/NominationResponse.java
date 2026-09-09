package com.example._2.dto;

import java.time.LocalDateTime;

public class NominationResponse {

    private Long id;
    private Long trainingProgrammeId;
    private String trainingTitle;
    private Long officerId;
    private String officerName;
    private String officerNic;
    private String officerEmail;
    private String nominatingDepartmentName;
    private String status;
    private LocalDateTime nominatedAt;

    public NominationResponse() {
    }

    public NominationResponse(Long id, Long trainingProgrammeId, String trainingTitle, Long officerId, String officerName, String officerNic, String officerEmail, String nominatingDepartmentName, String status, LocalDateTime nominatedAt) {
        this.id = id;
        this.trainingProgrammeId = trainingProgrammeId;
        this.trainingTitle = trainingTitle;
        this.officerId = officerId;
        this.officerName = officerName;
        this.officerNic = officerNic;
        this.officerEmail = officerEmail;
        this.nominatingDepartmentName = nominatingDepartmentName;
        this.status = status;
        this.nominatedAt = nominatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrainingProgrammeId() {
        return trainingProgrammeId;
    }

    public void setTrainingProgrammeId(Long trainingProgrammeId) {
        this.trainingProgrammeId = trainingProgrammeId;
    }

    public String getTrainingTitle() {
        return trainingTitle;
    }

    public void setTrainingTitle(String trainingTitle) {
        this.trainingTitle = trainingTitle;
    }

    public Long getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Long officerId) {
        this.officerId = officerId;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getOfficerNic() {
        return officerNic;
    }

    public void setOfficerNic(String officerNic) {
        this.officerNic = officerNic;
    }

    public String getOfficerEmail() {
        return officerEmail;
    }

    public void setOfficerEmail(String officerEmail) {
        this.officerEmail = officerEmail;
    }

    public String getNominatingDepartmentName() {
        return nominatingDepartmentName;
    }

    public void setNominatingDepartmentName(String nominatingDepartmentName) {
        this.nominatingDepartmentName = nominatingDepartmentName;
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
