package com.example._2.service.eligibility;

import com.example._2.model.TrainingProgramme;
import com.example._2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EligibilityEngine {

    @Autowired
    private List<EligibilityRule> rules;

    public void evaluateAll(User officer, TrainingProgramme training) {
        if (rules != null) {
            for (EligibilityRule rule : rules) {
                rule.validate(officer, training);
            }
        }
    }
}
