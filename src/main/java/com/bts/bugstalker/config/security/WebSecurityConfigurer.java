package com.bts.bugstalker.config.security;

import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.feature.cache.jwt.JwtHelper;
import com.bts.bugstalker.util.parameters.ApiPaths;
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

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Value("${bs.web.security.debug:false}")
    boolean webSecurityDebug;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper;

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    private final LogoutSuccessHandler logoutSuccessHandler;

    private final JwtHelper jwtHelper;

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
                .logout()
                .logoutUrl(ApiPaths.SIGN_OUT)
                .logoutSuccessHandler(logoutSuccessHandler)

                .and()
                .addFilter(loginAuthenticationFilter())
                .addFilter(jwtAuthFilter())
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        http.headers().frameOptions().disable();
    }

    @Bean
    public LoginAuthFilter loginAuthenticationFilter() throws Exception {
        LoginAuthFilter filter = new LoginAuthFilter(objectMapper);
        filter.setFilterProcessesUrl(ApiPaths.SIGN_IN);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() throws Exception {
        return new JwtAuthFilter(authenticationManager(), userDetailsService, jwtHelper);
    }
}
