package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.EmailRequest;
import org.example.dto.ItineraryRequest;
import org.example.dto.ItineraryResponse;
import org.example.model.entity.Itinerary;
//import org.example.model.entity.User;
import org.example.model.entity.Users;
import org.example.repository.ItineraryRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final UserRepository userRepository;
    private final TextProcessingService textProcessingService;
    private final EmailService emailService;

    public ItineraryResponse saveItinerary(Long userId, ItineraryRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Itinerary itinerary = Itinerary.builder()
                .user(user)
                .destination(request.getDestination())
                .fullItinerary(request.getFullItinerary())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .numberOfDays(request.getNumberOfDays())
                .budgetRange(request.getBudgetRange())
                .travelStyle(request.getTravelStyle())
                .build();

        Itinerary savedItinerary = itineraryRepository.save(itinerary);
        sendEmail(user, request);
        return mapToResponse(savedItinerary);
    }

    public List<ItineraryResponse> getUserItineraries(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Itinerary> itineraries = itineraryRepository.findByUserOrderByCreatedAtDesc(user);
        return itineraries.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ItineraryResponse getItineraryById(Long userId, Long itineraryId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        if (!itinerary.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponse(itinerary);
    }

    public List<ItineraryResponse> searchItineraries(Long userId, String searchTerm) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String processedSearchTerm = textProcessingService.processText(searchTerm);
        List<Itinerary> itineraries = itineraryRepository.findByUserOrderByCreatedAtDesc(user);
        return itineraries.stream().filter(itinerary -> {
            String processedDestination = textProcessingService.processText(itinerary.getDestination());
            String processedItinerary = textProcessingService.processText(itinerary.getFullItinerary());
            return processedDestination.contains(processedSearchTerm) ||
                    processedItinerary.contains(processedSearchTerm);
        }).map(this::mapToResponse).collect(Collectors.toList());
    }

    public void deleteItinerary(Long userId, Long itineraryId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        if (!itinerary.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        itineraryRepository.delete(itinerary);
    }

    private ItineraryResponse mapToResponse(Itinerary itinerary) {
        return ItineraryResponse.builder()
                .id(itinerary.getId())
                .destination(itinerary.getDestination())
                .fullItinerary(itinerary.getFullItinerary())
                .startDate(itinerary.getStartDate())
                .endDate(itinerary.getEndDate())
                .numberOfDays(itinerary.getNumberOfDays())
                .budgetRange(itinerary.getBudgetRange())
                .travelStyle(itinerary.getTravelStyle())
                .createdAt(itinerary.getCreatedAt())
                .updatedAt(itinerary.getUpdatedAt())
                .build();
    }

    public void sendEmail(Users user, ItineraryRequest request) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setMessage("Thank you for checking your travel itinerary with us! We hope the AI-powered recommendations have helped you plan your trip.\n" +
                "\n" +
                "For even better support and personalized assistance, we invite you to explore our premium plans, starting at very affordable prices. With premium access, you'll enjoy priority support and exclusive features tailored to make your travel experience even more seamless.\n" +
                "\n" +
                "If you have any questions or need help enrolling, feel free to reach out!");
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject("Thank You for Checking Your Itinerary! ✨");
        emailService.sendEmail(emailRequest);
    }
}