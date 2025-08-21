package com.devsync_backend.securityconfig.config;

import com.devsync_backend.securityconfig.service.CustomUserDetailsService;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService  customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(CustomUserDetailsService customUserDetailsService , PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication == null) throw new BadCredentialsException("UserName and Password not found !");
        String userName = authentication.getName(); // Take UserName
        String password = authentication.getCredentials().toString(); // Take Password
        UserDetails fetchedUser = customUserDetailsService.loadUserByUsername(userName);

        if(fetchedUser == null) throw new BadCredentialsException("User Not Found");

        if(userName.equals(fetchedUser.getUsername()) && passwordEncoder.matches(password,fetchedUser.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    userName,
                    password,
                    fetchedUser.getAuthorities() // Passing Authorities User has in a successful Authentication Token
            );
        }

        throw new BadCredentialsException("Incorrect UserName or Password !");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
