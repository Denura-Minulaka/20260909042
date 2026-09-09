package com.example._2.service.eligibility;

import com.example._2.exception.IneligibleOfficerException;
import com.example._2.model.Nomination;
import com.example._2.model.TrainingProgramme;
import com.example._2.model.User;
import com.example._2.repository.NominationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class TwelveMonthCooldownEligibilityRule implements EligibilityRule {

    @Autowired
    private NominationRepository nominationRepository;

    @Override
    public void validate(User officer, TrainingProgramme training) {
        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusYears(1);

        Optional<Nomination> pastNomination = nominationRepository
                .findFirstByTrainingProgrammeIdAndOfficerIdAndNominatedAtAfterOrderByNominatedAtDesc(
                        training.getId(), officer.getId(), twelveMonthsAgo);

        if (pastNomination.isPresent()) {
            Nomination n = pastNomination.get();
            String formattedDate = n.getNominatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            throw new IneligibleOfficerException(
                    String.format("Officer '%s' participated in training '%s' on %s. Re-registration is not allowed within 12 months.",
                            officer.getFullName(), training.getTitle(), formattedDate)
            );
        }
    }
}
