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
        // Step 1: Ensure initial departments exist
        Department deptITMD = departmentRepository.findByCode("ITMD")
                .orElseGet(() -> departmentRepository.save(new Department("Information Technology Management Dept", "ITMD")));
        Department deptFinance = departmentRepository.findByCode("FINANCE")
                .orElseGet(() -> departmentRepository.save(new Department("Finance Division", "FINANCE")));
        Department deptAdmin = departmentRepository.findByCode("ADMIN")
                .orElseGet(() -> departmentRepository.save(new Department("Administration Division", "ADMIN")));

        // Step 2: Update or Create Users with complete grade & yearsOfService values
        createOrUpdateUser("Training Coordinator", "coordinator@treasury.gov.lk", "admin123", "199011111111", "ROLE_COORDINATOR", deptITMD, "Executive", 10);
        createOrUpdateUser("Finance Head", "depthead.fin@treasury.gov.lk", "head123", "199122222222", "ROLE_DEPT_HEAD", deptFinance, "Executive", 8);
        createOrUpdateUser("Admin Head", "depthead.adm@treasury.gov.lk", "head123", "199233333333", "ROLE_DEPT_HEAD", deptAdmin, "Executive", 7);

        // Officers for Task 1, 2 & 3 testing
        User perera = createOrUpdateUser("A. Perera", "perera@treasury.gov.lk", "officer123", "199512345678", "ROLE_OFFICER", deptFinance, "Grade I", 5);
        User silva = createOrUpdateUser("B. Silva", "silva@treasury.gov.lk", "officer123", "199687654321", "ROLE_OFFICER", deptAdmin, "Grade II", 1);
        createOrUpdateUser("C. Fernando", "fernando@treasury.gov.lk", "officer123", "199733344455", "ROLE_OFFICER", deptITMD, "Grade I", 8);
        createOrUpdateUser("D. Gamage", "gamage@treasury.gov.lk", "officer123", "199855566677", "ROLE_OFFICER", deptFinance, "Grade II", 2);

        // Step 3: Ensure Venues exist
        Venue venueAuditorium = venueRepository.findAll().stream().findFirst()
                .orElseGet(() -> venueRepository.save(new Venue("New Auditorium, Ministry of Finance", 100, "Ground Floor, Main Building")));

        // Step 4: Ensure Trainers exist
        Trainer trainerJayawardena = trainerRepository.findAll().stream().findFirst()
                .orElseGet(() -> trainerRepository.save(new Trainer("Dr. K. Jayawardena", "EXTERNAL", "Java Spring Boot Enterprise Architecture")));

        // Step 5: Update or Create Training Programmes with Task 3 Eligibility fields
        List<TrainingProgramme> trainings = trainingRepository.findAll();
        if (trainings.isEmpty()) {
            Set<Department> targetDepts = new HashSet<>(List.of(deptITMD, deptFinance, deptAdmin));
            TrainingProgramme training1 = new TrainingProgramme(
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
            training1.setMinYearsOfService(2);
            training1.setRequiredGrade("Grade I");
            trainingRepository.save(training1);
        } else {
            // Populate null fields for existing trainings
            for (TrainingProgramme t : trainings) {
                if (t.getMinYearsOfService() == null) t.setMinYearsOfService(2);
                if (t.getRequiredGrade() == null) t.setRequiredGrade("Grade I");
                trainingRepository.save(t);
            }
        }

        System.out.println(">>> GTMS System Data & Task 3 Eligibility Fields Populated Successfully <<<");
    }

    private User createOrUpdateUser(String name, String email, String password, String nic, String role, Department dept, String grade, int years) {
        return userRepository.findByEmail(email).map(user -> {
            user.setGrade(grade);
            user.setYearsOfService(years);
            user.setDepartment(dept);
            return userRepository.save(user);
        }).orElseGet(() -> {
            User newUser = new User(name, email, passwordEncoder.encode(password), nic, role, dept, grade, years);
            return userRepository.save(newUser);
        });
    }
}
