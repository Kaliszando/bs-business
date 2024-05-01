package com.bts.bugstalker.config.security;

import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.util.parameters.ApiPaths;
import com.bts.bugstalker.util.properties.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Value("${bs.web.security.debug:false}")
    boolean webSecurityDebug;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper;

    private final SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler;

    private final SimpleUrlAuthenticationFailureHandler authenticationFailureHandler;

    private final JwtProperties jwtProperties;

    @Override
    public void configure(WebSecurity web) {
        web.debug(webSecurityDebug);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(ApiPaths.PUBLIC).permitAll()
                .antMatchers(ApiPaths.ADMIN, ApiPaths.PING).hasAuthority(UserRole.ADMIN.getCode())
                .anyRequest().authenticated()

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilter(loginAuthenticationFilter())
                .addFilter(new JwtAuthFilter(authenticationManager(), userDetailsService, jwtProperties))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        http.headers().frameOptions().disable();
    }

    @Bean
    public LoginAuthFilter loginAuthenticationFilter() throws Exception {
        LoginAuthFilter filter = new LoginAuthFilter(objectMapper);
        filter.setFilterProcessesUrl(ApiPaths.SIGN_IN);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setAuthenticationManager(super.authenticationManager());
        return filter;
    }
}
