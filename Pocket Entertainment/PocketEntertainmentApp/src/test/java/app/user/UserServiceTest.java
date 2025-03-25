package app.user;

import app.exception.UserNotFound;
import app.exception.UsernameAlreadyExists;
import app.security.AuthenticationMetadata;
import app.user.model.Role;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.wallet.model.Currency;
import app.wallet.service.WalletService;
import app.web.dto.EditAccountRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private UserService userService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .isActive(true)
                .build();
    }

    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "password");
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        userService.registerUser(request);

        verify(userRepository, times(1)).save(any(User.class));
        verify(walletService, times(2)).createWallet(any(User.class), any(Currency.class));
    }

    @Test
    void testRegisterUser_WithRoleUser() {
        RegisterRequest request = new RegisterRequest("randomemail@email.com", "randomusername", "password");
        when(userRepository.findAll()).thenReturn(List.of(user));

        userService.registerUser(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_ThrowsException_WhenUsernameOrEmailExists() {
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "password");
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UsernameAlreadyExists.class, () -> userService.registerUser(request));
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        User foundUser = userService.getUserById(userId);
        assertEquals(userId, foundUser.getId());
    }

    @Test
    void testGetUserById_ThrowsException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFound.class, () -> userService.getUserById(userId));
    }

    @Test
    void testEditUser_Success() {
        EditAccountRequest editRequest = EditAccountRequest.builder()
                .email("test@example.com")
                .firstName("test")
                .lastName("tester")
                .build();
        userService.editUser(user, editRequest);

        assertEquals("test", user.getFirstName());
        assertEquals("tester", user.getLastName());
        assertEquals("test@example.com", user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        AuthenticationMetadata userDetails = (AuthenticationMetadata) userService.loadUserByUsername("testuser");
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_ThrowsException_WhenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        assertThrows(UserNotFound.class, () -> userService.loadUserByUsername("nonexistent"));
    }

    @Test
    void testChangeRole_Success() {
        assertEquals(Role.USER, user.getRole());
        userService.changeRole(user);
        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangeRole_TogglesRole() {
        user.setRole(Role.ADMIN);
        userService.changeRole(user);
        assertEquals(Role.USER, user.getRole());
        verify(userRepository, times(1)).save(user);
    }
}

