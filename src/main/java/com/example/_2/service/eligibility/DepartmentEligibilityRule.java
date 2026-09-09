package com.example._2.service.eligibility;

import com.example._2.exception.IneligibleOfficerException;
import com.example._2.model.Department;
import com.example._2.model.TrainingProgramme;
import com.example._2.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DepartmentEligibilityRule implements EligibilityRule {

    @Override
    public void validate(User officer, TrainingProgramme training) {
        Set<Department> targetDepts = training.getTargetDepartments();

        if (targetDepts != null && !targetDepts.isEmpty() && officer.getDepartment() != null) {
            boolean isTargeted = targetDepts.stream()
                    .anyMatch(d -> d.getId().equals(officer.getDepartment().getId()));

            if (!isTargeted) {
                String allowedNames = targetDepts.stream()
                        .map(Department::getName)
                        .collect(Collectors.joining(", "));

                throw new IneligibleOfficerException(
                        String.format("Officer '%s' (%s) is not eligible. Training '%s' is restricted to department(s): %s.",
                                officer.getFullName(), officer.getDepartment().getName(), training.getTitle(), allowedNames)
                );
            }
        }
    }
}
