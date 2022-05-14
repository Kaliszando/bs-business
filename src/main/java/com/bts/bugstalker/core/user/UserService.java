package com.bts.bugstalker.core.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User create(final User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw UserExceptionFactory.createUserEmailIsTakenException(user.getEmail());
                });

        return userRepository.save(user);
    }

    public User getUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> UserExceptionFactory.createUserNotFoundException(userId));
    }

    public User getUser(final String userId) {
        return getUser(Long.valueOf(userId));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
