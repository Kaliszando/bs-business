package com.bts.bugstalker.config.security;

import com.bts.bugstalker.core.jwt.JwtService;
import com.bts.bugstalker.core.jwt.JwtUtility;
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

    private final JwtService jwtService;

    public JwtAuthFilter(AuthenticationManager authenticationManager,
                         UserDetailsService userDetailsService,
                         JwtService jwtService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
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
        String token = jwtService.extractToken(JwtUtility.getAuthHeaderContent(request)).orElse(null);
        if (token == null) {
            return null;
        }

        if (jwtService.isBlacklisted(token)) {
            return null;
        }

        String username = jwtService.extractUsernameFromToken(token);
        if (username == null) {
            return null;
        }

        UserDetails user = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }
}
