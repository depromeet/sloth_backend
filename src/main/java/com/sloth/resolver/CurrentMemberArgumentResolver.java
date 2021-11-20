package com.sloth.resolver;

import com.sloth.config.auth.TokenProvider;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class CurrentMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MemberRepository memberRepository;

    public CurrentMemberArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasEmailAnnotation = parameter.hasParameterAnnotation(CurrentMember.class);
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasEmailAnnotation && hasMemberType;
    }

    @Transactional
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = TokenProvider.getMemberEmail(token);
        return memberRepository.findByEmail(email).orElseThrow( () -> new UsernameNotFoundException("해당 회원 정보가 없습니다. email : " + email));
    }

}
