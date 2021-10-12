package com.sloth.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.config.auth.dto.TokenDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Transactional
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private MemberRepository memberRepository;
    private TokenProvider tokenProvider;

    public LoginSuccessHandler(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException
            , EntityNotFoundException {

        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        String email = (String) principal.getAttributes().get("email");

        TokenDto tokenDto = tokenProvider.createTokenDto(email);
        saveRefreshToken(email, tokenDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToken = objectMapper.writeValueAsString(tokenDto);

        String role = getMemberRole(authentication);

        if(Role.USER.getKey().equals(role)) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write(jsonToken);
            out.flush();
            out.close();
        } else if(Role.ADMIN.getKey().equals(role)) {
            response.sendRedirect("/");
        }
    }

    /**
     * refresh token 저장
     * @param email
     * @param tokenDto
     */
    private void saveRefreshToken(String email, TokenDto tokenDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Date refreshTokenExpireTime = tokenDto.getRefreshTokenExpireTime();

        LocalDateTime tokenExpiredTime = DateTimeUtils.convertToLocalDateTime(refreshTokenExpireTime);

        MemberToken memberToken = MemberToken.createMemberToken(member, tokenDto.getRefreshToken(), tokenExpiredTime);
        member.setMemberToken(memberToken);
        memberRepository.save(member);
    }

    /**
     * 로그인 성공한 사용자 ROLE 조회
     * @param authentication
     * @return 로그인 성공한 회원의 ROLE 반환
     */
    private String getMemberRole(Authentication authentication) {
        String role = "";
        Collection<? extends GrantedAuthority> authorities1 = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities1) {
            role = grantedAuthority.getAuthority();
        }
        return role;
    }

}