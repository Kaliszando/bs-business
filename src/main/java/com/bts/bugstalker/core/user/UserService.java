package com.bts.bugstalker.core.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserEntity getByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> UserExceptionFactory.userNotFoundException(username));
    }

    public UserEntity getById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> UserExceptionFactory.userNotFoundException(id));
    }

    public UserEntity create(final UserEntity user) {
        if (!isEmailAvailable(user.getEmail())) {
            throw UserExceptionFactory.userEmailIsTakenException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<UserEntity> queryByPhrase(final String query) {
        return (query == null)
                ? Collections.emptyList()
                : userRepository.searchByQuery(query.toUpperCase().replaceAll("\\s+",""));
    }

    public boolean isEmailAvailable(final String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .orElseThrow(() -> UserExceptionFactory.loginDoesNotMatchAnyUserException(login));
    }
}
