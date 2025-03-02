package com.pockEtentertainmentApp.review.service;

import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.review.model.Review;
import com.pockEtentertainmentApp.review.repository.ReviewRepository;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.web.dto.AddReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public void addReview(AddReviewRequest addReviewRequest, User user) {
        Review review = Review.builder()
                .body(addReviewRequest.getBody())
                .game(addReviewRequest.getGame())
                .rating(addReviewRequest.getRating())
                .user(user)
                .createdAt(LocalDate.now())
                .build();
        reviewRepository.save(review);
    }
    public List<Review> findByGame(UUID id) {

        return reviewRepository.findReviewsByGameId(id);

    }

}
