package com.example._2.service.impl;

import com.example._2.dto.CreateNominationRequest;
import com.example._2.dto.NominationResponse;
import com.example._2.exception.CapacityExceededException;
import com.example._2.exception.DuplicateNominationException;
import com.example._2.exception.ResourceNotFoundException;
import com.example._2.model.Department;
import com.example._2.model.Nomination;
import com.example._2.model.TrainingProgramme;
import com.example._2.model.User;
import com.example._2.repository.DepartmentRepository;
import com.example._2.repository.NominationRepository;
import com.example._2.repository.TrainingProgrammeRepository;
import com.example._2.repository.UserRepository;
import com.example._2.security.UserPrincipal;
import com.example._2.service.NominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NominationServiceImpl implements NominationService {

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private TrainingProgrammeRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public NominationResponse createNomination(Long trainingId, CreateNominationRequest request, UserPrincipal currentUser) {
        // 1. Verify Training Programme exists
        TrainingProgramme training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new ResourceNotFoundException("Training programme not found with ID: " + trainingId));

        // 2. Verify Officer exists
        User officer = userRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("Officer not found with ID: " + request.getOfficerId()));

        // 3. Determine nominating department
        Department nominatingDepartment;
        if (currentUser.getDepartmentId() != null) {
            nominatingDepartment = departmentRepository.findById(currentUser.getDepartmentId())
                    .orElse(officer.getDepartment());
        } else {
            nominatingDepartment = officer.getDepartment();
        }

        if (nominatingDepartment == null) {
            throw new ResourceNotFoundException("Nominating department could not be determined for officer");
        }

        // 4. TASK 1 VALIDATION: Check for Duplicate Nomination
        Optional<Nomination> existingNomination = nominationRepository
                .findByTrainingProgrammeIdAndOfficerId(trainingId, officer.getId());

        if (existingNomination.isPresent()) {
            Nomination existing = existingNomination.get();
            String existingDeptName = existing.getNominatingDepartment() != null
                    ? existing.getNominatingDepartment().getName()
                    : "another department";

            throw new DuplicateNominationException(
                    String.format("Officer '%s' (NIC: %s) has already been nominated for the training '%s' by the %s.",
                            officer.getFullName(), officer.getNic(), training.getTitle(), existingDeptName)
            );
        }

        // 5. Capacity Check
        long currentCount = nominationRepository.countByTrainingProgrammeId(trainingId);
        if (currentCount >= training.getMaxParticipants()) {
            throw new CapacityExceededException(
                    String.format("Training programme '%s' has reached its maximum participant limit of %d.",
                            training.getTitle(), training.getMaxParticipants())
            );
        }

        // 6. Save Nomination
        Nomination nomination = new Nomination(
                training,
                officer,
                nominatingDepartment,
                "PENDING"
        );

        Nomination saved = nominationRepository.save(nomination);
        return mapToResponse(saved);
    }

    @Override
    public List<NominationResponse> getNominationsByTrainingId(Long trainingId) {
        if (!trainingRepository.existsById(trainingId)) {
            throw new ResourceNotFoundException("Training programme not found with ID: " + trainingId);
        }

        return nominationRepository.findByTrainingProgrammeId(trainingId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public NominationResponse updateNominationStatus(Long nominationId, String status) {
        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new ResourceNotFoundException("Nomination not found with ID: " + nominationId));

        nomination.setStatus(status.toUpperCase());
        Nomination updated = nominationRepository.save(nomination);
        return mapToResponse(updated);
    }

    private NominationResponse mapToResponse(Nomination nomination) {
        return new NominationResponse(
                nomination.getId(),
                nomination.getTrainingProgramme().getId(),
                nomination.getTrainingProgramme().getTitle(),
                nomination.getOfficer().getId(),
                nomination.getOfficer().getFullName(),
                nomination.getOfficer().getNic(),
                nomination.getOfficer().getEmail(),
                nomination.getNominatingDepartment() != null ? nomination.getNominatingDepartment().getName() : "N/A",
                nomination.getStatus(),
                nomination.getNominatedAt()
        );
    }
}
