package com.bts.bugstalker.core.user;

import com.bts.bugstalker.core.user.exception.UserLoginDoesNotMatchAnyResultException;
import com.bts.bugstalker.core.user.exception.UserEmailAlreadyTakenException;
import com.bts.bugstalker.core.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    private final UserRepositoryImpl userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity getByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserEntity getById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserEntity create(final UserEntity user) {
        if (!isEmailAvailable(user.getEmail())) {
            throw new UserEmailAlreadyTakenException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<UserEntity> queryByPhrase(final String query) {
        return (StringUtils.isBlank(query))
                ? Collections.emptyList()
                : userRepository.searchByQuery(query);
    }

    public boolean isEmailAvailable(final String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .orElseThrow(() -> new UserLoginDoesNotMatchAnyResultException(login));
    }
}
