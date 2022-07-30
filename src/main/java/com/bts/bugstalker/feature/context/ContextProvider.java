package com.bts.bugstalker.feature.context;

import com.bts.bugstalker.core.user.UserEntity;
import com.bts.bugstalker.core.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContextProvider {

    private final UserService userService;

    private UserEntity contextUser;

    public UserEntity getContextUser() {
        String contextUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (contextUser != null && contextUsername.equals(contextUser.getUsername())) {
            return contextUser;
        }

        contextUser = userService.getByUsername(contextUsername);
        return contextUser;
    }
}
