package com.pockEtentertainmentApp.user.service;

import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.repository.UserRepository;
import com.pockEtentertainmentApp.web.dto.LoginRequest;
import com.pockEtentertainmentApp.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User login(LoginRequest loginRequest){
        Optional<User> optional = userRepository.findByUsername(loginRequest.getUsername());

        if(optional.isEmpty()){
            throw new RuntimeException("User not found");
        }
        User user = optional.get();
        if (!user.getPassword().equals(loginRequest.getPassword())){
            throw new RuntimeException("Wrong password");
        }

        return user;
    }

    public void registerUser(RegisterRequest registerRequest){

        Optional<User> optional = userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail());

        if (optional.isPresent()){
            throw new RuntimeException("Username or email already in use");
        }

        new User();
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();


        userRepository.save(user);
    }

    public UUID getId(User user){

        return user.getId();
    }

    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
