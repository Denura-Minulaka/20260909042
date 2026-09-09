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
    @DisplayName("Task 1: Creating a new nomination succeeds when no duplicate exists")
    void testCreateNomination_Success() {
        CreateNominationRequest request = new CreateNominationRequest(4L);

        when(trainingRepository.findById(10L)).thenReturn(Optional.of(trainingJava));
        when(userRepository.findById(4L)).thenReturn(Optional.of(officerPerera));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(deptFinance));
        when(nominationRepository.findByTrainingProgrammeIdAndOfficerId(10L, 4L)).thenReturn(Optional.empty());
        when(nominationRepository.countByTrainingProgrammeId(10L)).thenReturn(5L);

        Nomination savedNomination = new Nomination(
                100L,
                trainingJava,
                officerPerera,
                deptFinance,
                "PENDING",
                LocalDateTime.now()
        );
        when(nominationRepository.save(any(Nomination.class))).thenReturn(savedNomination);

        NominationResponse response = nominationService.createNomination(10L, request, currentUserPrincipal);

        assertNotNull(response);
        assertEquals("A. Perera", response.getOfficerName());
        assertEquals("199512345678", response.getOfficerNic());
        assertEquals("Finance Division", response.getNominatingDepartmentName());
        verify(nominationRepository, times(1)).save(any(Nomination.class));
    }

    @Test
    @DisplayName("Task 1: Submitting duplicate nomination throws DuplicateNominationException")
    void testCreateNomination_ThrowsDuplicateNominationException() {
        CreateNominationRequest request = new CreateNominationRequest(4L);

        when(trainingRepository.findById(10L)).thenReturn(Optional.of(trainingJava));
        when(userRepository.findById(4L)).thenReturn(Optional.of(officerPerera));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(deptFinance));

        Nomination existingNomination = new Nomination(
                99L,
                trainingJava,
                officerPerera,
                deptFinance,
                "PENDING",
                LocalDateTime.now().minusDays(1)
        );
        when(nominationRepository.findByTrainingProgrammeIdAndOfficerId(10L, 4L)).thenReturn(Optional.of(existingNomination));

        DuplicateNominationException exception = assertThrows(
                DuplicateNominationException.class,
                () -> nominationService.createNomination(10L, request, currentUserPrincipal)
        );

        assertTrue(exception.getMessage().contains("Officer 'A. Perera' (NIC: 199512345678) has already been nominated"));
        verify(nominationRepository, never()).save(any(Nomination.class));
    }

    @Test
    @DisplayName("Submitting nomination when max capacity reached throws CapacityExceededException")
    void testCreateNomination_ThrowsCapacityExceededException() {
        CreateNominationRequest request = new CreateNominationRequest(4L);

        when(trainingRepository.findById(10L)).thenReturn(Optional.of(trainingJava));
        when(userRepository.findById(4L)).thenReturn(Optional.of(officerPerera));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(deptFinance));
        when(nominationRepository.findByTrainingProgrammeIdAndOfficerId(10L, 4L)).thenReturn(Optional.empty());
        when(nominationRepository.countByTrainingProgrammeId(10L)).thenReturn(50L); // Max capacity 50 reached!

        CapacityExceededException exception = assertThrows(
                CapacityExceededException.class,
                () -> nominationService.createNomination(10L, request, currentUserPrincipal)
        );

        assertTrue(exception.getMessage().contains("reached its maximum participant limit of 50"));
        verify(nominationRepository, never()).save(any(Nomination.class));
    }
}
