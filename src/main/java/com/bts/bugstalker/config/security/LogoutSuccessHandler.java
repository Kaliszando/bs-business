package com.bts.bugstalker.config.security;

import com.bts.bugstalker.feature.cache.jwt.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final JwtHelper jwtHelper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        jwtHelper.extractToken(request).ifPresentOrElse(
                token -> onSuccess(token, response),
                () -> onError(response)
        );
    }

    private void onSuccess(String token, HttpServletResponse response) {
        jwtHelper.addToBlacklist(token);
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    private void onError(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
