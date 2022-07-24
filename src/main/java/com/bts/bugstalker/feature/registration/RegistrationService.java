package com.bts.bugstalker.feature.registration;

import com.bts.bugstalker.core.user.UserEntity;
import com.bts.bugstalker.core.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegistrationService {

    private final UserService userService;

    public void registerUser(UserEntity user) {
        // check if email available

        // map to entity

        // check if entity valid

        // persist user as disabled and inactive

        // send email with verification token
    }

    public boolean confirmToken(UserEntity user, String token) {
        // check if match

        // check if not expired

        // enable user

        return true;
    }
}
