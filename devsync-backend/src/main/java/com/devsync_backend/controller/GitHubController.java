package com.devsync_backend.controller;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import static org.springframework.security.oauth2.client.web.ClientAttributes.clientRegistrationId;


// End point of the user repositories
@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final RestClient restClient;

    public GitHubController(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/repositories")
    public ResponseEntity<String> getUserRepositories(Authentication authentication) {

        // The RestClient automatically adds the OAuth2 token for the current user
        String responseBody =
                restClient.get()
                .uri("https://api.github.com/user/repos")
                .attributes(clientRegistrationId("github"))
                .retrieve()
                .body(String.class);

        return ResponseEntity.ok(responseBody);
    }

}

