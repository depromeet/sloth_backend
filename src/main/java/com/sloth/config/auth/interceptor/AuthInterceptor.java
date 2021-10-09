package com.sloth.config.auth.interceptor;

import com.sloth.config.auth.TokenProvider;
import com.sloth.config.auth.constant.TokenType;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.repository.MemberTokenRepository;
import com.sloth.exception.BusinessException;
import com.sloth.exception.MemberNotFoundException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final MemberTokenRepository memberTokenRepository;
    private final MemberRepository memberRepository;
    private final MessageSource messageSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader == null || StringUtils.isBlank(authorizationHeader)) {
            log.info("토큰 정보 없음");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "토큰정보가 없습니다.");
            return false;
        }

        String token = authorizationHeader.replace("Bearer", "").trim();

        if(!tokenProvider.validateToken(token)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "잘못된 토큰 정보 입니다.");
            return false;
        }

        Claims tokenClaims = tokenProvider.getTokenClaims(token);

        String tokenType = tokenClaims.getSubject();
        String email = tokenClaims.getAudience();
        Date expiration = tokenClaims.getExpiration();

        if(TokenType.ACCESS.name().equals(tokenType)) {

            //access token 만료
            if(tokenProvider.isTokenExpired(expiration)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Access Token이 만료되었습니다.");
                return false;
            }

        } else if(TokenType.REFRESH.name().equals(tokenType)) {

            //refresh token 만료 안됐으면 access token 재발급해서 반환
            if(!tokenProvider.isTokenExpired(expiration)) {
                Date accessTokenExpireTime = tokenProvider.createAccessTokenExpireTime();
                String accessToken = tokenProvider.createAccessToken(email, accessTokenExpireTime);
                response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);

                // 회원정보 조회
                log.info(messageSource.getMessage("memberNotFound", null, null));
                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new MemberNotFoundException(
                                messageSource.getMessage("memberNotFound", null, null))
                        );

                // 리프레시 토큰 만료 시간 갱신
                updateRefreshTokenExpireTime(token);

            } else if(tokenProvider.isTokenExpired(expiration)) {   //refresh token이 만료 됐을 경우
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Refresh Token이 만료되었습니다.");
            }

        }

        return true;
    }

    /**
     * 리프레시 토큰 만료시간 갱신
     * @param token
     */
    private void updateRefreshTokenExpireTime(String token) {
        MemberToken memberToken = memberTokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new BusinessException("해당 토큰이 존재하지 않습니다."));

        LocalDateTime tokenExpirationTime = memberToken.getTokenExpirationTime();
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), tokenExpirationTime);
        if(hours <= 72) {   //토큰 만료 시간이 72시간 이하일 경우 토큰 만료 시간 갱신
            log.info("토큰 만료 시간 갱신 : {}", hours);
            memberToken.updateTokenExpirationTime(LocalDateTime.now().plusWeeks(2));
        }
    }

}
