package com.bts.bugstalker.core.user;

import com.bts.bugstalker.core.user.exception.UserEmailAlreadyTakenException;
import com.bts.bugstalker.core.user.exception.UserLoginDoesNotMatchAnyResultException;
import com.bts.bugstalker.core.user.exception.UserNotFoundException;
import com.bts.bugstalker.util.properties.UserProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements UserDetailsService {

    private final UserRepositoryImpl userRepository;

    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = UserProperties.USER_BY_USERNAME, key = "#username")
    public UserEntity getByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @CachePut(value = UserProperties.USER_BY_USERNAME, key = "#user.username")
    public UserEntity create(final UserEntity user) {
        if (!isEmailAvailable(user.getEmail())) {
            throw new UserEmailAlreadyTakenException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Caching(evict = {
        @CacheEvict(value = UserProperties.USER_BY_USERNAME, key = "#user.username"),
        @CacheEvict(value = UserProperties.USER_BY_USERNAME, key = "#user.email")
    })
    public void delete(final UserEntity user) {
        userRepository.delete(user);
    }

    @CacheEvict(value = {UserProperties.USER_BY_USERNAME}, allEntries = true)
    @Scheduled(fixedRateString = "${user.cache.ttl.min}", timeUnit = TimeUnit.MINUTES)
    public void invalidateUserCache() {
        log.info("Invalidating user cache");
    }

    public List<UserEntity> queryByParam(final String query, final Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }
        return userRepository.searchByQuery(query, projectId);
    }

    public boolean isEmailAvailable(final String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    @Cacheable(value = UserProperties.USER_BY_USERNAME, key = "#login")
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .orElseThrow(() -> new UserLoginDoesNotMatchAnyResultException(login));
    }
}
