package com.sloth.global.resolver;

import com.sloth.global.config.auth.TokenProvider;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.global.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class CurrentMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private MemberRepository memberRepository;

    public CurrentMemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
        return memberRepository.findByEmail(email).orElseThrow( () -> new BusinessException("해당 회원 정보가 없습니다. email : " + email));
    }

}
