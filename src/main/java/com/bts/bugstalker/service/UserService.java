package com.bts.bugstalker.service;

import com.bts.bugstalker.exception.user.UserAlreadyExistsException;
import com.bts.bugstalker.exception.user.UserNotFoundException;
import com.bts.bugstalker.model.User;
import com.bts.bugstalker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(found -> {
                    throw new UserAlreadyExistsException(found.getEmail());
                });

        return userRepository.save(user);
    }

    public User getUser(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(UserNotFoundException::new);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
