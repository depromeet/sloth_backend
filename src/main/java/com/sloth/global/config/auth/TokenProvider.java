package com.sloth.global.config.auth;

import com.sloth.global.config.auth.constant.TokenType;
import com.sloth.global.config.auth.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    private static final String BEARER_TYPE = "bearer";

    /**
     * TokenDto 생성
     * @param email
     * @return
     */
    public TokenDto createTokenDto(String email) {

        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(email, accessTokenExpireTime);
        String refreshToken = createRefreshToken(email, refreshTokenExpireTime);
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    /**
     * access token 만료 시간 생성
     * @return
     */
    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() +
                Long.parseLong(accessTokenExpirationTime));
    }

    /**
     * refresh token 만료 시간 생성
     * @return
     */
    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() +
                Long.parseLong(refreshTokenExpirationTime));
    }

    /**
     * access token 생성
     * @param email
     * @param expirationTime
     * @return access token 반환
     */
    public String createAccessToken(String email, Date expirationTime) {
        String accessToken = Jwts.builder()
                .setSubject(TokenType.ACCESS.name())            //토큰 제목
                .setAudience(email)                             //토큰 대상자
                .setIssuedAt(new Date())                        //토큰 발급 시간
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
        return accessToken;
    }

    /**
     * refresh token 생성
     * @param email
     * @param expirationTime
     * @return refresh token 반환
     */
    public String createRefreshToken(String email, Date expirationTime) {
        String refreshToken = Jwts.builder()
                .setSubject(TokenType.REFRESH.name())           //토큰 제목
                .setAudience(email)                             //토큰 대상자
                .setIssuedAt(new Date())                        //토큰 발급 시간
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
        return refreshToken;
    }

    /**
     * token이 만료됐는지 검사
     * @param tokenExpiredTime
     * @return
     */
    public boolean isTokenExpired(Date tokenExpiredTime) {

        Date now = new Date();

        if(now.after(tokenExpiredTime)) { //토큰 만료된 경우
            return true;
        }

        return false;
    }

    /**
     * token이 만료됐는지 검사
     * @param tokenExpiredTime
     * @return
     */
    public boolean isTokenExpired(LocalDateTime tokenExpiredTime) {

        if(LocalDateTime.now().isAfter(tokenExpiredTime)) { //토큰 만료된 경우
            return true;
        }

        return false;
    }

    /**
     * 토큰 파싱 후 property 반환
     * @param token
     * @return
     */
    public Claims getTokenClaims(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(tokenSecret)  //jwt 만들 때 사용했던 키
                    .parseClaimsJws(token).getBody()
            ;
        } catch (Exception e) {
            throw e;
        }

        return claims;
    }

    /**
     * 토큰 파싱 후 email 반환
     * @param token
     * @return
     */
    public String getEmail(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(tokenSecret)  //jwt 만들 때 사용했던 키
                    .parseClaimsJws(token).getBody()
            ;
        } catch (Exception e) {
            throw e;
        }

        return claims.getAudience();
    }

    public static String getMemberEmail(String token) {
        String email;
        try {
            Claims claims = Jwts.parser().setSigningKey("sloth-backend")  //jwt 만들 때 사용했던 키. static 메서드 사용하기 위해서 String으로 하드 코딩.
                    .parseClaimsJws(token).getBody();
            email = claims.getAudience();
        } catch (Exception e){
            e.printStackTrace();
            log.info("토큰 변환 중 에러 발생 : {}", token);
            throw e;
        }

        return email;
    }

    /**
     * 토큰 유효 검사
     * @param token
     * @return
     */
    public boolean validateToken(String token){

        try {
            Jwts.parser().setSigningKey(tokenSecret)
                    .parseClaimsJws(token);
            return true;
        } catch(JwtException e) {  //토큰 변조
            log.info("토큰 변조 감지 : {}", token);
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            log.info("토큰 검증 에러 발생 : {}", token);
            throw e;
        }

        return false;
    }

}
