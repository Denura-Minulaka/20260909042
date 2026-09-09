package com.example._2.service.impl;

import com.example._2.dto.CreateTrainingRequest;
import com.example._2.dto.TrainingResponse;
import com.example._2.exception.ResourceNotFoundException;
import com.example._2.model.Department;
import com.example._2.model.Trainer;
import com.example._2.model.TrainingProgramme;
import com.example._2.model.Venue;
import com.example._2.repository.*;
import com.example._2.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingProgrammeRepository trainingRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private NominationRepository nominationRepository;

    @Override
    public TrainingResponse createTraining(CreateTrainingRequest request) {
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + request.getVenueId()));

        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with ID: " + request.getTrainerId()));

        List<Department> targetDepartments = departmentRepository.findAllById(request.getTargetDepartmentIds());
        if (targetDepartments.isEmpty()) {
            throw new ResourceNotFoundException("No valid target departments found");
        }

        TrainingProgramme programme = new TrainingProgramme(
                null,
                request.getTitle(),
                request.getDescription(),
                request.getTrainingDate(),
                request.getMaxParticipants(),
                venue,
                trainer,
                new HashSet<>(targetDepartments),
                "PUBLISHED"
        );
        programme.setMinYearsOfService(request.getMinYearsOfService());
        programme.setRequiredGrade(request.getRequiredGrade());

        TrainingProgramme saved = trainingRepository.save(programme);
        return mapToResponse(saved);
    }

    @Override
    public TrainingResponse updateTraining(Long id, CreateTrainingRequest request) {
        TrainingProgramme programme = trainingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training programme not found with ID: " + id));

        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + request.getVenueId()));

        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with ID: " + request.getTrainerId()));

        List<Department> targetDepartments = departmentRepository.findAllById(request.getTargetDepartmentIds());

        programme.setTitle(request.getTitle());
        programme.setDescription(request.getDescription());
        programme.setTrainingDate(request.getTrainingDate());
        programme.setMaxParticipants(request.getMaxParticipants());
        programme.setVenue(venue);
        programme.setTrainer(trainer);
        if (!targetDepartments.isEmpty()) {
            programme.setTargetDepartments(new HashSet<>(targetDepartments));
        }
        programme.setMinYearsOfService(request.getMinYearsOfService());
        programme.setRequiredGrade(request.getRequiredGrade());

        TrainingProgramme updated = trainingRepository.save(programme);
        return mapToResponse(updated);
    }

    @Override
    public List<TrainingResponse> getAllTrainings() {
        return trainingRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TrainingResponse getTrainingById(Long id) {
        TrainingProgramme programme = trainingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training programme not found with ID: " + id));
        return mapToResponse(programme);
    }

    private TrainingResponse mapToResponse(TrainingProgramme programme) {
        long currentCount = nominationRepository.countByTrainingProgrammeId(programme.getId());
        List<String> targetDeptNames = programme.getTargetDepartments().stream()
                .map(Department::getName)
                .toList();

        TrainingResponse response = new TrainingResponse(
                programme.getId(),
                programme.getTitle(),
                programme.getDescription(),
                programme.getTrainingDate(),
                programme.getMaxParticipants(),
                currentCount,
                programme.getVenue() != null ? programme.getVenue().getName() : "N/A",
                programme.getVenue() != null ? programme.getVenue().getCapacity() : 0,
                programme.getTrainer() != null ? programme.getTrainer().getName() : "N/A",
                programme.getTrainer() != null ? programme.getTrainer().getType() : "N/A",
                targetDeptNames,
                programme.getStatus()
        );
        response.setMinYearsOfService(programme.getMinYearsOfService());
        response.setRequiredGrade(programme.getRequiredGrade());
        return response;
    }
}
