package app.review;

import app.game.model.Game;
import app.review.model.Review;
import app.review.repository.ReviewRepository;
import app.review.service.ReviewService;
import app.user.model.User;
import app.web.dto.AddReviewRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceUTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void findAllReturnsAllReviews() {
        Review review1 = new Review();
        Review review2 = new Review();
        Review review3 = new Review();

        List<Review> testReviews = List.of(review1, review2, review3);

        when(reviewRepository.findAll()).thenReturn(List.of(review1, review2, review3));

        List<Review> reviews = reviewService.findAll();

        assertEquals(testReviews, reviews);
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testAddReview_success() {
        AddReviewRequest dto = new AddReviewRequest();
        User user = new User();

        reviewService.addReview(dto, user);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testFindByGameId_returnsListOfReviews() {
        UUID id = UUID.randomUUID();
        Game game = new Game();
        game.setId(id);
        Review review1 = new Review();
        Review review2 = new Review();

        review1.setGame(game);
        review2.setGame(game);

        when(reviewRepository.findReviewsByGameId(id)).thenReturn(List.of(review1, review2));

        assertEquals(reviewService.findByGame(id), List.of(review1, review2));
    }



}
