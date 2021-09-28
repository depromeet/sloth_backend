package com.sloth.config.auth;

import com.sloth.domain.member.constant.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities1 = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities1) {
            String role = grantedAuthority.getAuthority();
            if(Role.USER.getKey().equals(role)) {
                response.sendRedirect("/");
                break;
            } else {
                response.sendRedirect("/api/members/login/success-callback");
                break;
            }
        }
    }

}
