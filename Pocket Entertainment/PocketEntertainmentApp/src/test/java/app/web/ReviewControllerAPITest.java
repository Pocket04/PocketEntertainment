package app.web;

import app.game.service.GameService;
import app.review.service.ReviewService;
import app.security.AuthenticationMetadata;
import app.user.model.Role;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ReviewController.class)
public class ReviewControllerAPITest {

    @MockitoBean
    private ReviewService reviewService;
    @MockitoBean
    private GameService gameService;
    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getReviews() throws Exception {

        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "testUser", "123123", Role.ADMIN, true);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/reviews")
                .with(user(metadata))
                .with(csrf());
        mockMvc.perform(builder)
                .andExpect(view().name("reviews"))
                .andExpect(model().attributeExists("games"))
                .andExpect(model().attributeExists("reviews"));

        verify(gameService,times(1)).getAllGames();
        verify(reviewService, times(1)).findAll();

    }
}
