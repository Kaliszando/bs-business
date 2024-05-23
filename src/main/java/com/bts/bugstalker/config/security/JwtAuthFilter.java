package com.bts.bugstalker.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bts.bugstalker.feature.cache.jwt.JwtHelper;
import com.bts.bugstalker.util.properties.JwtProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;

    private final JwtProperties jwtProperties;

    private final JwtHelper jwtHelper;

    public JwtAuthFilter(AuthenticationManager authenticationManager,
                         UserDetailsService userDetailsService,
                         JwtProperties jwtProperties,
                         JwtHelper jwtHelper) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtProperties = jwtProperties;
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        if (authenticationToken == null) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        if (!jwtHelper.hasAuthToken(request)) {
            return null;
        }

        String token = request.getHeader(JwtHelper.AUTH_HEADER_NAME)
                .replace(JwtHelper.AUTH_TOKEN_PREFIX, "");
        if (jwtHelper.isBlacklisted(token)) {
            return null;
        }

        String username = JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                .build()
                .verify(token)
                .getSubject();
        if (username == null) {
            return null;
        }

        UserDetails user = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }
}
