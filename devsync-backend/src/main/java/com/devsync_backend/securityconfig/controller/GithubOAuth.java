package com.devsync_backend.securityconfig.controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class GithubOAuth {

    // 1) Land here after successful GitHub OAuth2 login
    @GetMapping("/oauth2/success")
    public Map<String, Object> success(
            Authentication authentication,
            @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient client) {

        Map<String, Object> resp = new HashMap<>();
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        OAuth2AccessToken token = client.getAccessToken();

        resp.put("message", "GitHub login successful");
        resp.put("principal_attributes", principal.getAttributes());  // GitHub user JSON
        resp.put("token_type", token.getTokenType().getValue());
        resp.put("token_expires_at", token.getExpiresAt());
        resp.put("scopes", token.getScopes());

        // Tip for Postman: grab JSESSIONID from Set-Cookie, then call /github/* endpoints
        return resp;
    }

    // 2) Quick check of who is logged in (attributes from GitHub /user)
    @GetMapping("/github/user")
    public Map<String, Object> currentUser(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }

    // 3) Commits endpoint: GET /github/commits?owner={owner}&repo={repo}&since=YYYY-MM-DDTHH:MM:SSZ&until=...
    @GetMapping("/github/commits")
    public ResponseEntity<String> commits(
            @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient client,
            @RequestParam String owner,
            @RequestParam String repo,
            @RequestParam(required = false) String since,  // ISO 8601; optional
            @RequestParam(required = false) String until   // ISO 8601; optional
    ) {
        StringBuilder url = new StringBuilder("https://api.github.com/repos/")
                .append(owner).append("/").append(repo).append("/commits");

        boolean hasQuery = false;
        if (since != null && !since.isBlank()) {
            url.append(hasQuery ? "&" : "?").append("since=").append(since);
            hasQuery = true;
        }
        if (until != null && !until.isBlank()) {
            url.append(hasQuery ? "&" : "?").append("until=").append(until);
        }

        String token = client.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/vnd.github+json"); // recommended
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        return rt.exchange(url.toString(), HttpMethod.GET, entity, String.class);
    }
}
