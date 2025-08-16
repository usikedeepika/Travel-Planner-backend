package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ReviewRequest;
import org.example.dto.ReviewResponse;
import org.example.model.entity.Review;
//import org.example.model.entity.User;
import org.example.model.entity.Users;
import org.example.repository.ReviewRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<ReviewResponse> getAllReviews() {
        List<Review> reviews = reviewRepository.findAllByOrderByCreatedAtDesc();
        return mapToResponse(reviews);
    }

    public ReviewResponse submitResponse(ReviewRequest request, String userName) {
        Review review = new Review();
        review.setReviewerName(userName);
        review.setRating(request.getRating());

        Users user = userRepository.findByUsername(userName).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found: " + userName);
        }

        review.setComment(request.getComment());
        review.setUser(user);
        review.setTitle(request.getTitle());
        review.setDestination(request.getDestination());

        Review savedReview = reviewRepository.save(review);
        return mapToReviewResponse(savedReview);
    }

    private List<ReviewResponse> mapToResponse(List<Review> reviews) {
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review : reviews) {
            reviewResponses.add(mapToReviewResponse(review));
        }
        return reviewResponses;
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviewerName(review.getReviewerName());
        reviewResponse.setId(review.getId());
        reviewResponse.setComment(review.getComment());
        reviewResponse.setRating(review.getRating());
        reviewResponse.setDestination(review.getDestination());
        reviewResponse.setCreatedAt(review.getCreatedAt());
        reviewResponse.setTitle(review.getTitle());
        return reviewResponse;
    }
}
