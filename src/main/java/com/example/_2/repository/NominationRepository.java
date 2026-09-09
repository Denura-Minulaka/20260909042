package com.example._2.repository;

import com.example._2.model.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NominationRepository extends JpaRepository<Nomination, Long> {

    // TASK 1: Check duplicate nomination by training and officer
    boolean existsByTrainingProgrammeIdAndOfficerId(Long trainingProgrammeId, Long officerId);

    // TASK 1: Find existing nomination to construct detailed duplicate error message
    Optional<Nomination> findByTrainingProgrammeIdAndOfficerId(Long trainingProgrammeId, Long officerId);

    // Get count of nominations for a specific status
    long countByTrainingProgrammeIdAndStatus(Long trainingProgrammeId, String status);

    // Get total count of nominations for a training programme
    long countByTrainingProgrammeId(Long trainingProgrammeId);

    // TASK 2: Find earliest nomination on waiting list for automatic promotion upon cancellation
    Optional<Nomination> findFirstByTrainingProgrammeIdAndStatusOrderByNominatedAtAsc(Long trainingProgrammeId, String status);

    // TASK 2: Retrieve ordered waiting list (First-Come, First-Served order)
    List<Nomination> findByTrainingProgrammeIdAndStatusOrderByNominatedAtAsc(Long trainingProgrammeId, String status);

    // List all nominations for a training programme (replaces manual Excel merging)
    List<Nomination> findByTrainingProgrammeId(Long trainingProgrammeId);

    // List nominations for a specific department
    List<Nomination> findByNominatingDepartmentId(Long departmentId);
}
