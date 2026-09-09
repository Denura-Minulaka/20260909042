package com.example._2.util;

import com.example._2.model.*;
import com.example._2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingProgrammeRepository trainingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (departmentRepository.count() > 0) {
            return; // Data already initialized
        }

        // 1. Initial Departments
        Department deptITMD = departmentRepository.save(new Department("Information Technology Management Dept", "ITMD"));
        Department deptFinance = departmentRepository.save(new Department("Finance Division", "FINANCE"));
        Department deptAdmin = departmentRepository.save(new Department("Administration Division", "ADMIN"));

        // 2. Initial Users
        User coordinator = new User(
                "Training Coordinator",
                "coordinator@treasury.gov.lk",
                passwordEncoder.encode("admin123"),
                "199011111111",
                "ROLE_COORDINATOR",
                deptITMD
        );
        userRepository.save(coordinator);

        User finHead = new User(
                "Finance Head",
                "depthead.fin@treasury.gov.lk",
                passwordEncoder.encode("head123"),
                "199122222222",
                "ROLE_DEPT_HEAD",
                deptFinance
        );
        userRepository.save(finHead);

        User admHead = new User(
                "Admin Head",
                "depthead.adm@treasury.gov.lk",
                passwordEncoder.encode("head123"),
                "199233333333",
                "ROLE_DEPT_HEAD",
                deptAdmin
        );
        userRepository.save(admHead);

        User officerPerera = new User(
                "A. Perera",
                "perera@treasury.gov.lk",
                passwordEncoder.encode("officer123"),
                "199512345678",
                "ROLE_OFFICER",
                deptFinance
        );
        userRepository.save(officerPerera);

        User officerSilva = new User(
                "B. Silva",
                "silva@treasury.gov.lk",
                passwordEncoder.encode("officer123"),
                "199687654321",
                "ROLE_OFFICER",
                deptAdmin
        );
        userRepository.save(officerSilva);

        // 3. Initial Venue
        Venue venueAuditorium = venueRepository.save(new Venue(
                "New Auditorium, Ministry of Finance",
                100,
                "Ground Floor, Main Building"
        ));

        // 4. Initial Trainer
        Trainer trainerJayawardena = trainerRepository.save(new Trainer(
                "Dr. K. Jayawardena",
                "EXTERNAL",
                "Java Spring Boot Enterprise Architecture"
        ));

        // 5. Initial Training Programme
        Set<Department> targetDepts = new HashSet<>(List.of(deptITMD, deptFinance, deptAdmin));
        TrainingProgramme training = new TrainingProgramme(
                null,
                "Java Spring Boot Enterprise Architecture",
                "Comprehensive training for Ministry IT officers on enterprise Spring Boot REST APIs, Security, and Database integration.",
                LocalDate.now().plusDays(14),
                50,
                venueAuditorium,
                trainerJayawardena,
                targetDepts,
                "PUBLISHED"
        );
        trainingRepository.save(training);

        System.out.println(">>> GTMS System Data Initialized Successfully <<<");
        System.out.println(">>> Admin / Coordinator: coordinator@treasury.gov.lk / admin123");
        System.out.println(">>> Finance Dept Head: depthead.fin@treasury.gov.lk / head123");
        System.out.println(">>> Officer A. Perera (ID 4, NIC 199512345678): perera@treasury.gov.lk");
    }
}
