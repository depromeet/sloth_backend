package com.sloth.resolver;

import com.sloth.config.auth.TokenProvider;
import com.sloth.domain.member.dto.MemberInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class MemberInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasEmailAnnotation = parameter.hasParameterAnnotation(Member.class);
        boolean hasMemberInfoType = MemberInfo.class.isAssignableFrom(parameter.getParameterType());

        return hasEmailAnnotation && hasMemberInfoType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = TokenProvider.getMemberEmail(token);
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setEmail(email);
        return memberInfo;
    }

}
