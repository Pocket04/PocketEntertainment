package com.pockEtentertainmentApp.user.service;

import com.pockEtentertainmentApp.security.AuthenticationMetadata;
import com.pockEtentertainmentApp.user.model.Role;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.repository.UserRepository;
import com.pockEtentertainmentApp.wallet.model.Currency;
import com.pockEtentertainmentApp.wallet.model.Wallet;
import com.pockEtentertainmentApp.wallet.service.WalletService;
import com.pockEtentertainmentApp.web.dto.EditAccountRequest;
import com.pockEtentertainmentApp.web.dto.LoginRequest;
import com.pockEtentertainmentApp.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

        Optional<User> optional = userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail());

        if (optional.isPresent()){
            throw new RuntimeException("Username or email already in use");
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
        return userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
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

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole(), user.isActive());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void makeAdmin(User user) {
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
}
