package com.bts.bugstalker.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bts.bugstalker.util.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@RequiredArgsConstructor
@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProperties jwtProperties;

    private final static String AUTH_HEADER_NAME = "Authorization";

    private final static String AUTH_TOKEN_PREFIX = "Bearer ";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        // todo move to JwtHelper
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTimeMillis()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));

        response.addHeader(AUTH_HEADER_NAME, AUTH_TOKEN_PREFIX + token);

        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
