package com.devsync_backend.securityconfig.mappers;

import com.devsync_backend.securityconfig.model.SignUpRequest;
import com.devsync_backend.securityconfig.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User signUpToUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUserName(signUpRequest.getUserName());
        user.setUserRole(signUpRequest.getUserRole());
        user.setPassword(signUpRequest.getPassword());

        return user;
    }

    public SignUpRequest userToSignUpRequest(User normalUser) {
        SignUpRequest user = new SignUpRequest();
        user.setUserName(normalUser.getUsername());
        user.setUserRole(normalUser.getUserRole());
        user.setPassword(normalUser.getPassword());

        return user;
    }
}
