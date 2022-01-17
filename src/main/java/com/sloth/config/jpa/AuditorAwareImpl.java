package com.sloth.config.jpa;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        String modifiedBy;

        if(authentication != null && !"anonymousUser".equals(authentication.getPrincipal())) {
            modifiedBy = authentication.getName();
        } else {
            modifiedBy = httpServletRequest.getRequestURI();
        }

        if(StringUtils.isEmpty(modifiedBy)){
            modifiedBy = "unknown";
        }

        return Optional.of(modifiedBy);
    }

}