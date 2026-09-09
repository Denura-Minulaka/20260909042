package com.example._2.service;

import com.example._2.dto.CreateTrainingRequest;
import com.example._2.dto.TrainingResponse;

import java.util.List;

public interface TrainingService {
    TrainingResponse createTraining(CreateTrainingRequest request);
    List<TrainingResponse> getAllTrainings();
    TrainingResponse getTrainingById(Long id);
}
