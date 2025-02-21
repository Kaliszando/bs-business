package com.bts.bugstalker.config.security;

import com.bts.bugstalker.common.model.LoginCredentials;
import com.bts.bugstalker.feature.auth.exception.AuthInvalidSignInRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            LoginCredentials loginCredentials = objectMapper.readValue(sb.toString(), LoginCredentials.class);
            if (StringUtils.isBlank(loginCredentials.email()) || StringUtils.isBlank(loginCredentials.password())) {
                throw new AuthInvalidSignInRequest(loginCredentials.toString());
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginCredentials.email().trim(), loginCredentials.password().trim()
            );

            setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
