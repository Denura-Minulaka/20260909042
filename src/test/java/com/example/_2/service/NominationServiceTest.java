package com.example._2.service;

import com.example._2.dto.CreateNominationRequest;
import com.example._2.dto.NominationResponse;
import com.example._2.exception.CapacityExceededException;
import com.example._2.exception.DuplicateNominationException;
import com.example._2.model.Department;
import com.example._2.model.Nomination;
import com.example._2.model.TrainingProgramme;
import com.example._2.model.User;
import com.example._2.repository.DepartmentRepository;
import com.example._2.repository.NominationRepository;
import com.example._2.repository.TrainingProgrammeRepository;
import com.example._2.repository.UserRepository;
import com.example._2.security.UserPrincipal;
import com.example._2.service.impl.NominationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NominationServiceTest {

    @Mock
    private NominationRepository nominationRepository;

    @Mock
    private TrainingProgrammeRepository trainingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private NominationServiceImpl nominationService;

    private Department deptFinance;
    private User officerPerera;
    private TrainingProgramme trainingJava;
    private UserPrincipal currentUserPrincipal;

    @BeforeEach
    void setUp() {
        deptFinance = new Department(1L, "Finance Division", "FINANCE");

        officerPerera = new User(
                4L,
                "A. Perera",
                "perera@treasury.gov.lk",
                "encodedPwd",
                "199512345678",
                "ROLE_OFFICER",
                deptFinance
        );

        trainingJava = new TrainingProgramme(
                10L,
                "Java Spring Boot Enterprise Architecture",
                "Enterprise Java Training",
                LocalDate.now().plusDays(10),
                50,
                null,
                null,
                Collections.singleton(deptFinance),
                "PUBLISHED"
        );

        currentUserPrincipal = new UserPrincipal(
                2L,
                "Finance Head",
                "depthead.fin@treasury.gov.lk",
                "headPwd",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_DEPT_HEAD")),
                1L,
                "Finance Division"
        );
    }

    @Test
    @DisplayName("Task 2: Creating a nomination under max capacity sets status to CONFIRMED (FCFS)")
    void testCreateNomination_UnderCapacity_StatusConfirmed() {
        CreateNominationRequest request = new CreateNominationRequest(4L);

        when(trainingRepository.findById(10L)).thenReturn(Optional.of(trainingJava));
        when(userRepository.findById(4L)).thenReturn(Optional.of(officerPerera));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(deptFinance));
        when(nominationRepository.findByTrainingProgrammeIdAndOfficerId(10L, 4L)).thenReturn(Optional.empty());
        when(nominationRepository.countByTrainingProgrammeIdAndStatus(10L, "CONFIRMED")).thenReturn(5L);

        Nomination savedNomination = new Nomination(
                100L,
                trainingJava,
                officerPerera,
                deptFinance,
                "CONFIRMED",
                LocalDateTime.now()
        );
        when(nominationRepository.save(any(Nomination.class))).thenReturn(savedNomination);

        NominationResponse response = nominationService.createNomination(10L, request, currentUserPrincipal);

        assertNotNull(response);
        assertEquals("CONFIRMED", response.getStatus());
        verify(nominationRepository, times(1)).save(any(Nomination.class));
    }

    @Test
    @DisplayName("Task 2: Submitting nomination exceeding capacity places officer on WAITING_LIST")
    void testCreateNomination_ExceedsCapacity_PlacedOnWaitingList() {
        CreateNominationRequest request = new CreateNominationRequest(4L);

        when(trainingRepository.findById(10L)).thenReturn(Optional.of(trainingJava));
        when(userRepository.findById(4L)).thenReturn(Optional.of(officerPerera));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(deptFinance));
        when(nominationRepository.findByTrainingProgrammeIdAndOfficerId(10L, 4L)).thenReturn(Optional.empty());
        when(nominationRepository.countByTrainingProgrammeIdAndStatus(10L, "CONFIRMED")).thenReturn(50L); // Max capacity 50!

        Nomination waitingNomination = new Nomination(
                101L,
                trainingJava,
                officerPerera,
                deptFinance,
                "WAITING_LIST",
                LocalDateTime.now()
        );
        when(nominationRepository.save(any(Nomination.class))).thenReturn(waitingNomination);

        NominationResponse response = nominationService.createNomination(10L, request, currentUserPrincipal);

        assertNotNull(response);
        assertEquals("WAITING_LIST", response.getStatus());
        verify(nominationRepository, times(1)).save(any(Nomination.class));
    }

    @Test
    @DisplayName("Task 2: Cancelling a CONFIRMED nomination automatically promotes the first officer on the WAITING_LIST")
    void testCancelNomination_AutoPromotesFirstWaitingListOfficer() {
        Nomination confirmedNomination = new Nomination(
                100L,
                trainingJava,
                officerPerera,
                deptFinance,
                "CONFIRMED",
                LocalDateTime.now().minusDays(2)
        );

        User officerSilva = new User(5L, "B. Silva", "silva@treasury.gov.lk", "pwd", "199687654321", "ROLE_OFFICER", deptFinance);
        Nomination waitingNomination = new Nomination(
                101L,
                trainingJava,
                officerSilva,
                deptFinance,
                "WAITING_LIST",
                LocalDateTime.now().minusDays(1)
        );

        when(nominationRepository.findById(100L)).thenReturn(Optional.of(confirmedNomination));
        when(nominationRepository.save(confirmedNomination)).thenReturn(confirmedNomination);
        when(nominationRepository.findFirstByTrainingProgrammeIdAndStatusOrderByNominatedAtAsc(10L, "WAITING_LIST"))
                .thenReturn(Optional.of(waitingNomination));

        NominationResponse response = nominationService.cancelNomination(100L, currentUserPrincipal);

        assertEquals("CANCELLED", response.getStatus());
        verify(nominationRepository, times(1)).save(confirmedNomination);
        // Verify that waiting list officer was saved with status CONFIRMED
        verify(nominationRepository, times(1)).save(waitingNomination);
        assertEquals("CONFIRMED", waitingNomination.getStatus());
    }
}
