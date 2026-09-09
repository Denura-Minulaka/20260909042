package com.example._2.service;

import com.example._2.dto.CreateNominationRequest;
import com.example._2.dto.NominationResponse;
import com.example._2.security.UserPrincipal;

import java.util.List;

public interface NominationService {
    NominationResponse createNomination(Long trainingId, CreateNominationRequest request, UserPrincipal currentUser);
    List<NominationResponse> getNominationsByTrainingId(Long trainingId);
    NominationResponse updateNominationStatus(Long nominationId, String status);
}
