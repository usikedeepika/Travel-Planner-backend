package org.example.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.aspectj.apache.bcel.generic.RET;
import org.example.dto.*;
//import org.example.model.entity.User;
import org.example.model.entity.Users;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username already Exists");
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        Users user = Users.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(hashedPassword)
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .build();

        Users savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtHelper.generateToken(savedUser.getUsername());

        return new AuthResponse(
                token,
                "Bearer",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRole()
        );
    }

    public AuthResponse Login(LoginRequest loginRequest) throws BadRequestException {
        // Validate that at least username or email is provided
        if ((loginRequest.getUserName() == null || loginRequest.getUserName().trim().isEmpty()) &&
                (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty())) {
            throw new BadRequestException("Username or email is required");
        }

        Users user = null;

        // Try to find user by username first, then by email
        if (loginRequest.getUserName() != null && !loginRequest.getUserName().trim().isEmpty()) {
            user = userRepository.findByUsername(loginRequest.getUserName()).orElse(null);
        } else if (loginRequest.getEmail() != null && !loginRequest.getEmail().trim().isEmpty()) {
            user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        }

        if (user == null) {
            throw new BadRequestException("User does not exist");
        }

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        // Generate JWT token
        String token = jwtHelper.generateToken(user.getUsername());

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }

    public Users getUserByUserName(UserProfileRequest userProfileRequest) {
        return userRepository.findByUsername(userProfileRequest.getUserName()).orElse(null);
    }

    public Boolean existByUserName(String userName) {
        return userRepository.existsByUsername(userName);
    }

    public Boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public AuthResponse googleAuth(GoogleAuthRequest googleAuthRequest) throws BadRequestException {
        if (googleAuthRequest.getUserData() == null || googleAuthRequest.getToken() == null) {
            throw new BadRequestException();
        }
        String email = googleAuthRequest.getUserData().getEmail();
        Users user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String token = jwtHelper.generateToken(user.getEmail());
            return new AuthResponse(token, "Bearer", user.getId(), user.getEmail(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole());
        }
        String userName=googleAuthRequest.getUserData().getEmail();
        Users newUser=Users.builder().username(userName).email(email).password(passwordEncoder.encode("GOOGLE_AUTH"+System.currentTimeMillis()))
                .firstName(googleAuthRequest.getUserData().getFirstName()).lastName(googleAuthRequest.getUserData().getLastName())
                .phoneNumber(null).build();
        Users savedUser=userRepository.save(newUser);
        String token=jwtHelper.generateToken(savedUser.getUsername());
        return new AuthResponse(token, "Bearer", savedUser.getId(), savedUser.getEmail(), savedUser.getEmail(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getRole());
    }
}