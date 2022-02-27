package com.sloth.global.config.auth.interceptor;

import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.constant.TokenRefreshCritnTime;
import com.sloth.domain.memberToken.exception.MemberTokenNotFoundException;
import com.sloth.domain.memberToken.repository.MemberTokenRepository;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.global.config.auth.constant.JwtTokenType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final MemberTokenRepository memberTokenRepository;

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
        log.info("token : {}", token);

        if(!tokenProvider.validateToken(token)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "잘못된 토큰 정보 입니다.");
            return false;
        }

        Claims tokenClaims = tokenProvider.getTokenClaims(token);

        String tokenType = tokenClaims.getSubject();
        String email = tokenClaims.getAudience();


        if(JwtTokenType.ACCESS.name().equals(tokenType)) {

            Date expiration = tokenClaims.getExpiration();

            //access token 만료
            if(tokenProvider.isTokenExpired(expiration)) {
                log.info("Access Token이 만료되었습니다.");
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Access Token이 만료되었습니다.");
                return false;
            }

        } else if(JwtTokenType.REFRESH.name().equals(tokenType)) {

            MemberToken memberToken = memberTokenRepository.findByRefreshToken(token)
                    .orElseThrow(() -> new MemberTokenNotFoundException("해당 리프레시 토큰이 존재하지 않습니다."));
            LocalDateTime refreshTokenExpirationTime = memberToken.getTokenExpirationTime();

            //refresh token 만료 안됐으면 access token 재발급 및 Authorization Header 세팅
            if(!tokenProvider.isTokenExpired(refreshTokenExpirationTime)) {
                Date accessTokenExpireTime = tokenProvider.createAccessTokenExpireTime();
                String accessToken = tokenProvider.createAccessToken(email, accessTokenExpireTime);
                response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);

                // 리프레시 토큰 만료 시간 갱신
                memberToken.updateRefreshTokenExpireTime(LocalDateTime.now(), TokenRefreshCritnTime.HOURS_72);

            } else if(tokenProvider.isTokenExpired(refreshTokenExpirationTime)) {   //refresh token이 만료 됐을 경우
                log.info("Refresh Token이 만료되었습니다.");
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Refresh Token이 만료되었습니다.");
                return false;
            }

        }

        return true;
    }

}
