package org.example.controller;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ApiResponse;
import org.example.dto.ItineraryRequest;
import org.example.dto.ItineraryResponse;

import org.example.model.entity.Users;
import org.example.repository.UserRepository;
import org.example.service.ItineraryService;
import org.example.service.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;

    private String getAuthenticatedUsername(HttpServletRequest request) {
//        return (String) request.getAttribute("authenticatedUsername");
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtHelper.extractUserName(token);  // use your JWT utility class
        }

        return null;
    }

    private boolean validateUserAccess(String tokenUsername, Long userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return tokenUsername.equals(user.getUsername());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItineraryResponse>> saveItinerary(
            HttpServletRequest request,
            @RequestParam Long userId,
            @Valid @RequestBody ItineraryRequest itineraryRequest) {

        try {
            String tokenUsername = getAuthenticatedUsername(request);
            if (!validateUserAccess(tokenUsername, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Token username does not match user"));
            }

            ItineraryResponse response = itineraryService.saveItinerary(userId, itineraryRequest);
            return ResponseEntity.ok(ApiResponse.success("Itinerary saved successfully", response));
        } catch (RuntimeException e) {
//            log.error("Error saving itinerary: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItineraryResponse>>> getUserItineraries(
            HttpServletRequest request,
            @RequestParam Long userId) {

        try {
            String tokenUsername = getAuthenticatedUsername(request);
            if (!validateUserAccess(tokenUsername, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Token username does not match user"));
            }

            List<ItineraryResponse> itineraries = itineraryService.getUserItineraries(userId);
            return ResponseEntity.ok(ApiResponse.success("User itineraries retrieved successfully", itineraries));
        } catch (RuntimeException e) {
//            log.error("Error fetching user itineraries: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{itineraryId}")
    public ResponseEntity<ApiResponse<ItineraryResponse>> getItineraryById(
            HttpServletRequest request,
            @RequestParam Long userId,
            @PathVariable Long itineraryId) {

        try {
            String tokenUsername = getAuthenticatedUsername(request);
            if (!validateUserAccess(tokenUsername, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Token username does not match user"));
            }

            ItineraryResponse itinerary = itineraryService.getItineraryById(userId, itineraryId);
            return ResponseEntity.ok(ApiResponse.success("Itinerary retrieved successfully", itinerary));
        } catch (RuntimeException e) {
//            log.error("Error fetching itinerary: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ItineraryResponse>>> searchItineraries(
            HttpServletRequest request,
            @RequestParam Long userId,
            @RequestParam String searchTerm) {

        try {
            String tokenUsername = getAuthenticatedUsername(request);
            if (!validateUserAccess(tokenUsername, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Token username does not match user"));
            }

            List<ItineraryResponse> itineraries = itineraryService.searchItineraries(userId, searchTerm);
            return ResponseEntity.ok(ApiResponse.success("Search completed successfully", itineraries));
        } catch (RuntimeException e) {
//            log.error("Error searching itineraries: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{itineraryId}")
    public ResponseEntity<ApiResponse<String>> deleteItinerary(
            HttpServletRequest request,
            @RequestParam Long userId,
            @PathVariable Long itineraryId) {

        try {
            String tokenUsername = getAuthenticatedUsername(request);
            if (!validateUserAccess(tokenUsername, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Token username does not match user"));
            }

            itineraryService.deleteItinerary(userId, itineraryId);
            return ResponseEntity.ok(ApiResponse.success("Itinerary deleted successfully", null));
        } catch (RuntimeException e) {
//            log.error("Error deleting itinerary: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}