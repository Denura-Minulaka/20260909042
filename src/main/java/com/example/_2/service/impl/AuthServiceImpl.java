package com.example._2.service.impl;

import com.example._2.dto.AuthResponse;
import com.example._2.dto.LoginRequest;
import com.example._2.dto.RegisterRequest;
import com.example._2.exception.ResourceNotFoundException;
import com.example._2.model.Department;
import com.example._2.model.User;
import com.example._2.repository.DepartmentRepository;
import com.example._2.repository.UserRepository;
import com.example._2.security.JwtTokenProvider;
import com.example._2.security.UserPrincipal;
import com.example._2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return new AuthResponse(
                token,
                principal.getId(),
                principal.getFullName(),
                principal.getUsername(),
                principal.getAuthorities().iterator().next().getAuthority(),
                principal.getDepartmentName()
        );
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        if (userRepository.existsByNic(request.getNic())) {
            throw new IllegalArgumentException("NIC is already registered");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));

        User user = new User(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNic(),
                request.getRole(),
                department
        );

        userRepository.save(user);

        // Auto login after registration
        return login(new LoginRequest(request.getEmail(), request.getPassword()));
    }
}
