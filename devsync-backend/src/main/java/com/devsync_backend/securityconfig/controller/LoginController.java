package com.devsync_backend.securityconfig.controller;


import com.devsync_backend.securityconfig.model.LoginRequest;
import com.devsync_backend.securityconfig.model.SignUpRequest;
import com.devsync_backend.securityconfig.service.CustomUserDetailsService;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public LoginController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest user) {
        Authentication userAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authentication = authenticationManager.authenticate(userAuthenticationToken);
        if(authentication.isAuthenticated()) {
            return ResponseEntity.status(200).body("Logged In SuccessFully !");
        }
        return ResponseEntity.status(401).body("Bad Credentials Sent");
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        if(userDetailsService.isUserPresent(signUpRequest.getUserName())) {
            return ResponseEntity.status(409).body("Conflict : Account already present");
        }

        SignUpRequest signUpRequestCheck = userDetailsService.createSignUpUser(signUpRequest);
        if(signUpRequest.getUserName() == null) return ResponseEntity.status(500).body("Some Error Occurred while signing you up");
        else return ResponseEntity.status(200).body("User Signed Up SuccessFully !");
    }
}
