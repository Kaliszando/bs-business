package com.bts.bugstalker.config.security;

import com.bts.bugstalker.feature.cache.jwt.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtHelper jwtHelper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        response.addHeader(JwtHelper.AUTH_HEADER_NAME, jwtHelper.createJwtTokenWithPrefix(principal.getUsername()));
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
