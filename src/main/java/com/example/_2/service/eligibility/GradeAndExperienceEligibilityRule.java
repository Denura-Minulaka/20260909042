package com.example._2.service.eligibility;

import com.example._2.exception.IneligibleOfficerException;
import com.example._2.model.TrainingProgramme;
import com.example._2.model.User;
import org.springframework.stereotype.Component;

@Component
public class GradeAndExperienceEligibilityRule implements EligibilityRule {

    @Override
    public void validate(User officer, TrainingProgramme training) {
        // 1. Check Minimum Years of Service requirement
        if (training.getMinYearsOfService() != null && training.getMinYearsOfService() > 0) {
            int officerYears = officer.getYearsOfService() != null ? officer.getYearsOfService() : 0;
            if (officerYears < training.getMinYearsOfService()) {
                throw new IneligibleOfficerException(
                        String.format("Officer '%s' has %d years of service. Training '%s' requires a minimum of %d years of service.",
                                officer.getFullName(), officerYears, training.getTitle(), training.getMinYearsOfService())
                );
            }
        }

        // 2. Check Grade / Designation requirement
        if (training.getRequiredGrade() != null && !training.getRequiredGrade().trim().isEmpty()) {
            String officerGrade = officer.getGrade() != null ? officer.getGrade() : "Unassigned";
            if (!training.getRequiredGrade().equalsIgnoreCase(officerGrade)) {
                throw new IneligibleOfficerException(
                        String.format("Officer '%s' holds grade '%s'. Training '%s' requires grade '%s'.",
                                officer.getFullName(), officerGrade, training.getTitle(), training.getRequiredGrade())
                );
            }
        }
    }
}
