package com.example._2.controller;

import com.example._2.dto.ApiResponse;
import com.example._2.dto.CreateNominationRequest;
import com.example._2.dto.NominationResponse;
import com.example._2.security.UserPrincipal;
import com.example._2.service.NominationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
@Tag(name = "Nominations", description = "Endpoints for managing officer nominations and duplicate prevention")
public class NominationController {

    @Autowired
    private NominationService nominationService;

    @PostMapping("/trainings/{trainingId}/nominations")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINATOR', 'ROLE_DEPT_HEAD')")
    @Operation(summary = "Submit Officer Nomination (Task 1: Duplicate Prevention)",
               description = "Submits an officer nomination. Automatically validates and prevents duplicate nominations.")
    public ResponseEntity<ApiResponse<NominationResponse>> submitNomination(
            @PathVariable Long trainingId,
            @Valid @RequestBody CreateNominationRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        NominationResponse response = nominationService.createNomination(trainingId, request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Officer nominated successfully", response));
    }

    @GetMapping("/trainings/{trainingId}/nominations")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINATOR', 'ROLE_DEPT_HEAD')")
    @Operation(summary = "View Combined Nominations (Replaces manual Excel merging)",
               description = "Retrieves real-time combined list of nominations for a training programme across all departments")
    public ResponseEntity<ApiResponse<List<NominationResponse>>> getNominationsByTraining(@PathVariable Long trainingId) {
        List<NominationResponse> responses = nominationService.getNominationsByTrainingId(trainingId);
        return ResponseEntity.ok(ApiResponse.success("Combined nominations retrieved successfully", responses));
    }

    @GetMapping("/trainings/{trainingId}/waiting-list")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINATOR', 'ROLE_DEPT_HEAD')")
    @Operation(summary = "View Waiting List (Task 2)",
               description = "Retrieves the ordered waiting list for a training programme in First-Come, First-Served order")
    public ResponseEntity<ApiResponse<List<NominationResponse>>> getWaitingListByTraining(@PathVariable Long trainingId) {
        List<NominationResponse> responses = nominationService.getWaitingListByTrainingId(trainingId);
        return ResponseEntity.ok(ApiResponse.success("Waiting list retrieved successfully", responses));
    }

    @PutMapping("/nominations/{nominationId}/status")
    @PreAuthorize("hasAuthority('ROLE_COORDINATOR')")
    @Operation(summary = "Approve / Reject Nomination", description = "Updates nomination status to APPROVED or REJECTED (Coordinator only)")
    public ResponseEntity<ApiResponse<NominationResponse>> updateNominationStatus(
            @PathVariable Long nominationId,
            @RequestParam String status) {

        NominationResponse response = nominationService.updateNominationStatus(nominationId, status);
        return ResponseEntity.ok(ApiResponse.success("Nomination status updated to " + status, response));
    }

    @PutMapping("/nominations/{nominationId}/cancel")
    @PreAuthorize("hasAnyAuthority('ROLE_COORDINATOR', 'ROLE_DEPT_HEAD', 'ROLE_OFFICER')")
    @Operation(summary = "Cancel Nomination & Trigger Auto-Promotion (Task 2)",
               description = "Cancels a nomination. If the participant was CONFIRMED, automatically promotes the first officer on the WAITING_LIST to CONFIRMED.")
    public ResponseEntity<ApiResponse<NominationResponse>> cancelNomination(
            @PathVariable Long nominationId,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        NominationResponse response = nominationService.cancelNomination(nominationId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Nomination cancelled successfully and waiting list auto-promoted", response));
    }
}
