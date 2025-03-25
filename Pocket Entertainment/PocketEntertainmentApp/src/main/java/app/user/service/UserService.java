package app.user.service;

import app.exception.UserNotFound;
import app.exception.UsernameAlreadyExists;
import app.security.AuthenticationMetadata;
import app.user.model.Role;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.wallet.model.Currency;
import app.wallet.service.WalletService;
import app.web.dto.EditAccountRequest;
import app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, WalletService walletService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
    }

    public void registerUser(RegisterRequest registerRequest){

        Optional<User> usernameOptional = userRepository.findByUsername(registerRequest.getUsername());
        Optional<User> emailOptional = userRepository.findByEmail(registerRequest.getEmail());

        if (usernameOptional.isPresent() || emailOptional.isPresent()) {
            throw new UsernameAlreadyExists("Username or email already in use.");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isActive(true)
                .build();

        if (!getAllUsers().isEmpty()){
            user.setRole(Role.USER);
        }else {
            user.setRole(Role.ADMIN);
        }


        userRepository.save(user);

        walletService.createWallet(user, Currency.POCKET_TOKEN);
        walletService.createWallet(user,Currency.EURO);


    }

    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new UserNotFound("User not found"));
    }
    public void editUser(User user, EditAccountRequest editAccountRequest){
        user.setFirstName(editAccountRequest.getFirstName());
        user.setLastName(editAccountRequest.getLastName());
        user.setEmail(editAccountRequest.getEmail());
        user.setProfilePicture(editAccountRequest.getProfilePicture());
        user.setEmail(editAccountRequest.getEmail());
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFound("User not found"));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole(), user.isActive());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void changeRole(User user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            user.setRole(Role.ADMIN);
        }else {
            user.setRole(Role.USER);
        }
        userRepository.save(user);
    }
}
