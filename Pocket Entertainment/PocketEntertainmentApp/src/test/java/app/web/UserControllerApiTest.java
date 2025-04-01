package app.web;

import app.security.AuthenticationMetadata;
import app.user.model.Role;
import app.user.model.User;
import app.user.service.UserService;
import app.wallet.model.Currency;
import app.wallet.model.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void UnauthorisedGetUsersRequest_throwsPageNotFound() throws Exception {

        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "testUser", "123123", Role.USER, true);

        MockHttpServletRequestBuilder request = get("/users")
                .with(user(metadata))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("exception"));

    }

    @Test
    void AuthorisedGetUsersRequest_throwsPageNotFound() throws Exception {
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

        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "testUser", "123123", Role.ADMIN, true);

        when(userService.getUserById(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/users")
                .with(user(metadata))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("wallets"))
                .andExpect(model().attributeExists("user"));

        verify(userService, times(1)).getAllUsers();
        verify(userService, times(1)).getUserById(any());
    }

    @Test
    void AuthenticatedGetAccountRequest_returnsAccountView() throws Exception {
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

        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "testUser", "123123", Role.USER, true);

        when(userService.getUserById(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/users/{id}", UUID.randomUUID())
                .with(user(metadata))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("account"));

        verify(userService, times(1)).getUserById(any());
    }
    @Test
    void UnauthenticatedGetAccountRequest_redirectsToLogIn() throws Exception {

        MockHttpServletRequestBuilder request = get("/users/{id}", UUID.randomUUID());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
        verify(userService, never()).getUserById(any());
    }
    @Test
    void UnauthenticatedChangeRoleRequest_throwsPageNotFound() throws Exception {

        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "testUser", "123123", Role.USER, true);

        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(metadata))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("exception"));
        verify(userService, never()).getUserById(any());
        verify(userService, never()).changeRole(any());
    }
    @Test
    void AuthenticatedChangeRoleRequest_changesRole() throws Exception {
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

        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "testUser", "123123", Role.ADMIN, true);

        when(userService.getUserById(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/users/{id}/role", user.getId())
                .with(user(metadata))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
        verify(userService, times(1)).getUserById(any());
        verify(userService, times(1)).changeRole(any());

    }
    @Test
    void AuthenticatedGetProfileRequest_returnsEditProfileView() throws Exception {
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

        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "testUser", "123123", Role.USER, true);

        when(userService.getUserById(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/users/{id}/profile", UUID.randomUUID())
                .with(user(metadata))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("editAccountRequest"))
                .andExpect(view().name("edit-profile"));

        verify(userService, times(1)).getUserById(any());
    }
    @Test
    void UnauthenticatedGetProfileRequest_redirectsToLogIn() throws Exception {

        MockHttpServletRequestBuilder request = get("/users/{id}/profile", UUID.randomUUID());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());

        verify(userService, never()).getUserById(any());
    }
    @Test
    void AuthenticatedPutProfileRequest_updatesProfileAndRedirects() throws Exception {
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .email("test@test.com")
                .username("test")
                .password("test")
                .role(Role.USER)
                .isActive(true)
                .build();

        AuthenticationMetadata metadata = new AuthenticationMetadata(userId, "testUser", "123123", Role.USER, true);

        when(userService.getUserById(userId)).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/users/{id}/profile", userId)
                .with(user(metadata))
                .with(csrf())
                .param("profilePicture", "https://valid.url/image.jpg")
                .param("firstName", "NewFirst")
                .param("lastName", "NewLast")
                .param("email", "newemail@test.com");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/" + userId));

        verify(userService, times(1)).getUserById(userId);
    }
    @Test
    void UnauthenticatedPutProfileRequest_redirectsToLogIn() throws Exception {
        UUID userId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = put("/users/{id}/profile", userId)
                .with(csrf()) // CSRF protection required for PUT requests
                .param("profilePicture", "https://valid.url/image.jpg")
                .param("firstName", "NewFirst")
                .param("lastName", "NewLast")
                .param("email", "newemail@test.com");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());

        verify(userService, never()).getUserById(any());
        verify(userService, never()).editUser(any(), any());
    }








}
