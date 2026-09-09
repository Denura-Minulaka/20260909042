package com.example._2.dto;

import jakarta.validation.constraints.NotNull;

public class CreateNominationRequest {

    @NotNull(message = "Officer ID is mandatory")
    private Long officerId;

    public CreateNominationRequest() {
    }

    public CreateNominationRequest(Long officerId) {
        this.officerId = officerId;
    }

    public Long getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Long officerId) {
        this.officerId = officerId;
    }
}
