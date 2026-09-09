package com.example._2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nic;

    @Column(nullable = false)
    private String role; // ROLE_COORDINATOR, ROLE_DEPT_HEAD, ROLE_OFFICER

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    // TASK 3: Eligibility attributes
    private String grade; // e.g., "Grade I", "Grade II", "Executive"

    private Integer yearsOfService; // e.g., 5

    public User() {
    }

    public User(String fullName, String email, String password, String nic, String role, Department department, String grade, Integer yearsOfService) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.nic = nic;
        this.role = role;
        this.department = department;
        this.grade = grade;
        this.yearsOfService = yearsOfService;
    }

    public User(Long id, String fullName, String email, String password, String nic, String role, Department department) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.nic = nic;
        this.role = role;
        this.department = department;
    }

    public User(String fullName, String email, String password, String nic, String role, Department department) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.nic = nic;
        this.role = role;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getYearsOfService() {
        return yearsOfService;
    }

    public void setYearsOfService(Integer yearsOfService) {
        this.yearsOfService = yearsOfService;
    }
}
