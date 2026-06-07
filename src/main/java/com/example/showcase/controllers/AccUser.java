package com.example.showcase.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import com.example.showcase.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://showcase-pd.ru"})
@RequestMapping("/api")
public class AccUser {
    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @GetMapping("/user")
    public Map<String, Object> getUserInfo(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();

        if (authentication instanceof OAuth2AuthenticationToken oauth) {
            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                    oauth.getAuthorizedClientRegistrationId(),
                    oauth.getName()
            );
            userInfo.put("accessToken", client.getAccessToken().getTokenValue());
            userInfo.put("attributes", oauth.getPrincipal().getAttributes());
            return userInfo;
        }

        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("name", user.getFullName() != null ? user.getFullName() : "dev");
            attrs.put("email", user.getEmail());
            attrs.put("picture", user.getImagePath());
            attrs.put("sub", String.valueOf(user.getId()));
            userInfo.put("accessToken", "");
            userInfo.put("attributes", attrs);
            return userInfo;
        }

        return userInfo;
    }

    @GetMapping("/principal")
    public Principal checkUser(Principal principal) {
        return principal;
    }
}
