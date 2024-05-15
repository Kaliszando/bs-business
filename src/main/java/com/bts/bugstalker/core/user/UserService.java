package com.bts.bugstalker.core.user;

import com.bts.bugstalker.core.user.exception.UserLoginDoesNotMatchAnyResultException;
import com.bts.bugstalker.core.user.exception.UserEmailAlreadyTakenException;
import com.bts.bugstalker.core.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "userByUsername", key = "#username")
    public UserEntity getByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @CachePut(value = "userByUsername", key = "#user.username")
    public UserEntity create(final UserEntity user) {
        if (!isEmailAvailable(user.getEmail())) {
            throw new UserEmailAlreadyTakenException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @CacheEvict(value = "userByUsername", key = "#user.username")
    public void delete(final UserEntity user) {
        userRepository.delete(user);
    }

    public List<UserEntity> queryByParam(final String query, final Long projectId) {
        if (StringUtils.isEmpty(query) && projectId == null) {
            return Collections.emptyList();
        }
        //TODO refactor -> query available only when projectId specified
        if (!StringUtils.isBlank(query)) {
            return userRepository.searchByQuery(query);
        }
        return userRepository.findByProjectId(projectId);
    }

    public boolean isEmailAvailable(final String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    @Cacheable(value = "userByLogin", key = "#login")
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .orElseThrow(() -> new UserLoginDoesNotMatchAnyResultException(login));
    }
}
