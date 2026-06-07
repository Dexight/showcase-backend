package com.example.showcase.config;

import com.example.showcase.entity.Role;
import com.example.showcase.entity.User;
import com.example.showcase.repository.UserRepository;
import com.example.showcase.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (!(principal instanceof OidcUser oidcUser)) {
            return;
        }
        String email = (String) oidcUser.getAttributes().get("email");
        String name = (String) oidcUser.getAttributes().get("name");

        if (email == null) {
            return;
        }

        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setFullName(name);
            Role role = roleService.getRoleById(1);
            user.setRole(role);
            userRepository.save(user);
        }
    }
}
