package com.devsync_backend.securityconfig.config;


import com.devsync_backend.securityconfig.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login", "/signUp",                 // your DB endpoints
                                "/oauth2/**",                        // initiate GitHub login
                                "/login/oauth2/**",                  // GitHub redirects here
                                "/oauth2/success",                   // we’ll return JSON after OAuth login
                                "/error",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/api/github/**")
                        .authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/oauth2/authorization/github")  // no UI; direct-start OAuth
                        .defaultSuccessUrl("/oauth2/success", true) // after GitHub, land here
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService customUserDetailsService) {
        return new ProviderManager(new CustomAuthenticationProvider(customUserDetailsService,passwordEncoder()));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}