package com.sloth.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.config.auth.dto.TokenDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.repository.MemberTokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collection;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private MemberRepository memberRepository;
    private MemberTokenRepository memberTokenRepository;
    private TokenProvider tokenProvider;

    public LoginSuccessHandler(MemberRepository memberRepository, MemberTokenRepository memberTokenRepository,
                               TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.memberTokenRepository = memberTokenRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException
            , EntityNotFoundException {

        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        String email = (String) principal.getAttributes().get("email");

        TokenDto tokenDto = tokenProvider.createTokenDto(email);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToken = objectMapper.writeValueAsString(tokenDto);

        String role = getMemberRole(authentication);
        saveRefreshToken(email, tokenDto.getRefreshToken());

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
     * @param refreshToken
     */
    private void saveRefreshToken(String email, String refreshToken) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        LocalDateTime tokenExpiredTime = LocalDateTime.now().plusDays(14);  // 여기 토큰만들 때 값으로 들어가게 수정
        MemberToken memberToken = MemberToken.createMemberToken(member, refreshToken, tokenExpiredTime);
        memberTokenRepository.save(memberToken);
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