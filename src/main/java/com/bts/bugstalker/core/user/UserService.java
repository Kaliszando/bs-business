package com.bts.bugstalker.core.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity create(final UserEntity user) {
        if (!isEmailAvailable(user.getEmail())) {
            throw UserExceptionFactory.createUserEmailIsTakenException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public UserEntity getUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> UserExceptionFactory.createUserNotFoundException(userId));
    }

    public UserEntity getUser(final String userId) {
        return getUser(Long.valueOf(userId));
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public boolean isEmailAvailable(final String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

}
