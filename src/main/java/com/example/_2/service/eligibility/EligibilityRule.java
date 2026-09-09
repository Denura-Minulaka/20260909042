package com.example._2.service.eligibility;

import com.example._2.model.TrainingProgramme;
import com.example._2.model.User;

public interface EligibilityRule {
    void validate(User officer, TrainingProgramme training);
}
