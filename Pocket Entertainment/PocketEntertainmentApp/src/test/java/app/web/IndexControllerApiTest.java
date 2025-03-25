package app.web;

import app.cosmetic.service.CosmeticService;
import app.game.service.GameService;
import app.notification.service.NotificationService;
import app.security.AuthenticationMetadata;
import app.user.model.Role;
import app.user.model.User;
import app.user.service.UserService;
import app.wallet.model.Currency;
import app.wallet.model.Wallet;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private CosmeticService cosmeticService;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private GameService gameService;

    @Test
    void getRequestToIndexEndPoint_returnsIndexView() throws Exception {

        MockHttpServletRequestBuilder request = get("/");

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"));
    }
    @Test
    void getRequestToRegister_returnsRegisterView() throws Exception {
        MockHttpServletRequestBuilder request = get("/register");
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("registerRequest", instanceOf(RegisterRequest.class)));
    }
    @Test
    void postRequestToRegister_happyPath() throws Exception {
        MockHttpServletRequestBuilder request = post("/register")
                .formField("email", "test@test.com")
                .formField("username", "test")
                .formField("password", "test")
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).registerUser(any());
    }
    @Test
    void postRequestToRegister_notHappyPath() throws Exception {
        MockHttpServletRequestBuilder request = post("/register")
                .formField("email", "")
                .formField("username", "")
                .formField("password", "")
                .with(csrf());
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"));

        verify(userService, never()).registerUser(any());

    }
    @Test
    void getRequestToLogin_returnsLoginView() throws Exception {
        MockHttpServletRequestBuilder request = get("/login");
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("loginRequest", instanceOf(LoginRequest.class)));
    }
    @Test
    void getRequestToLoginWithErrorParam_returnsLoginViewWithErrorParam() throws Exception {
        MockHttpServletRequestBuilder request = get("/login")
                .param("error", "");

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("loginRequest", instanceOf(LoginRequest.class)))
                .andExpect(model().attributeExists("errorMessage", "errorMessage"));
    }
    @Test
    void getAuthenticatedRequestToHome_returnsHomeView() throws Exception {
        Wallet ptWallet = new Wallet();
        ptWallet.setCurrency(Currency.POCKET_TOKEN);
        Wallet eurWallet = new Wallet();
        eurWallet.setCurrency(Currency.EURO);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@test.com")
                .username("test")
                .password("test")
                .wallets(List.of(ptWallet, eurWallet))
                .role(Role.USER)
                .isActive(true)
                .build();

        when(userService.getUserById(any())).thenReturn(user);

        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "testUser", "123123", Role.USER, true);

        MockHttpServletRequestBuilder request = get("/home")
                .with(user(metadata));
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("games"))
                .andExpect(model().attributeExists("cosmetics"))
                .andExpect(model().attributeExists("boughtCosmetics"))
                .andExpect(model().attributeExists("wallets"));
        verify(userService, times(1)).getUserById(any());
    }
    @Test
    void getUnauthenticatedRequestToHome_returnsHomeView() throws Exception {
        MockHttpServletRequestBuilder request = get("/home");
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
        verify(userService, times(0)).getUserById(any());
    }
    @Test
    void getAuthenticatedRequestToPOP_returnsPOPView() throws Exception {
        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "testUser", "123123", Role.USER, true);

        MockHttpServletRequestBuilder request = get("/planet-of-peace")
                .with(user(metadata));
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("POP"));
    }
    @Test
    void getUnauthenticatedRequestToPOP_redirectsToLogin() throws Exception {

        MockHttpServletRequestBuilder request = get("/planet-of-peace");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
    }
}
