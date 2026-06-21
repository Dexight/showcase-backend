package com.example.showcase.config;

import com.example.showcase.entity.Role;
import com.example.showcase.entity.User;
import com.example.showcase.primary_filling.PrimaryFillingMapper;
import com.example.showcase.repository.SuperAdminRepository;
import com.example.showcase.repository.UserRepository;
import com.example.showcase.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;
    private final SuperAdminRepository superAdminRepository;

    @Autowired
    private RoleService roleService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (!(principal instanceof OidcUser oidcUser)) {
            return;
        }
        String email = resolveEmail(oidcUser);
        String name = (String) oidcUser.getAttributes().get("name");

        if (email == null) {
            return;
        }
        boolean isSuperAdmin = superAdminRepository.existsByEmail(email);

        // 1) Уже есть запись с таким email — связывание не требуется
        if (userRepository.existsByEmail(email)) {
            if (isSuperAdmin) {
                User user = userRepository.findByEmail(email);
                user.setRole(roleService.getRoleById(4));
                userRepository.save(user);
            }
            return;
        }

        // 2) Fallback: пытаемся привязать старую запись (без email) по ФИО.
        //    В Azure-токене нет курса/группы, поэтому матчим только по имени и
        //    линкуем лишь при единственном однозначном совпадении.
        if (name != null) {
            List<User> candidates = userRepository.findByFullNameAndEmailIsNull(name);
            if (candidates.size() == 1) {
                User existing = candidates.getFirst();
                existing.setEmail(email);
                if (isSuperAdmin) {
                    existing.setRole(roleService.getRoleById(4));
                }
                userRepository.save(existing);
                return;
            }
            if (candidates.size() > 1) {
                log.warn("Ambiguous email linking for '{}': {} records without email, creating a new user",
                        name, candidates.size());
            }
        }

        // 3) Иначе создаём нового пользователя
        User user = new User();
        user.setEmail(email);
        user.setFullName(name);
        if (isSuperAdmin) {
            Role role = roleService.getRoleById(4);
            user.setRole(role);
        }
        else {
            Role role = roleService.getRoleById(1);
            user.setRole(role);
        }
        userRepository.save(user);
    }

    //email приоритетно, с fallback на preferred_username/upn (зависит от настроек tenant)
    private String resolveEmail(OidcUser oidcUser) {
        Object email = oidcUser.getAttributes().get("email");
        if (email == null) {
            email = oidcUser.getAttributes().get("preferred_username");
        }
        if (email == null) {
            email = oidcUser.getAttributes().get("upn");
        }
        return PrimaryFillingMapper.normalizeEmail(email instanceof String s ? s : null);
    }
}
