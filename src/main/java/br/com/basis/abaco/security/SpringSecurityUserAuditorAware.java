package br.com.basis.abaco.security;

import br.com.basis.abaco.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityUserAuditorAware implements AuditorAware<User> {

    @Override
    public User getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetails = (UserDetailsCustom)auth.getPrincipal();
        return userDetails.getUser();
    }
}
