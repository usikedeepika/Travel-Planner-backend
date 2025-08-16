package org.example.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryResponse {
    private Long id;
    private String destination;
    private String fullItinerary;
    private String startDate;
    private String endDate;
    private Integer numberOfDays;
    private String budgetRange;
    private String travelStyle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
