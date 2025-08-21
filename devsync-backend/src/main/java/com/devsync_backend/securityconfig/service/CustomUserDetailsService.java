package com.devsync_backend.securityconfig.service;

import com.devsync_backend.securityconfig.mappers.UserMapper;
import com.devsync_backend.securityconfig.model.SignUpRequest;
import com.devsync_backend.securityconfig.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserMapper userMapper;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByUserName(username);
        return user;
    }

    public boolean isUserPresent(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public SignUpRequest createSignUpUser(SignUpRequest signUpRequest) {
        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return userMapper.userToSignUpRequest(userRepository.save(userMapper.signUpToUser(signUpRequest)));
    }
}
