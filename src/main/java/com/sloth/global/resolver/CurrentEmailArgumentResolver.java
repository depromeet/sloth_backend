package com.sloth.global.resolver;

import com.sloth.global.config.auth.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class CurrentEmailArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasEmailAnnotation = parameter.hasParameterAnnotation(CurrentEmail.class);
        boolean hasString = String.class.isAssignableFrom(parameter.getParameterType());

        return hasEmailAnnotation && hasString;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = TokenProvider.getMemberEmail(token);
        return email;
    }

}
