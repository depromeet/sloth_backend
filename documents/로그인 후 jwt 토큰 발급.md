### **๐ OAuth ๋ก๊ทธ์ธ, ํผ ๋ก๊ทธ์ธ ํ jwt ํ ํฐ ๋ฐ๊ธ๋ฐ๊ธฐ**
<br>

### ํด๊ฒฐ ๋ฐฉ๋ฒ
***
ํด๋ผ์ด์ธํธ์๊ฒ accessToken์ ๋ฐ์ ํ ์ฌ๋ฐ๋ฅธ ํ ํฐ ๊ฐ์ด๋ฉด LoginService์ ์๋ login ๋ฉ์๋๋ฅผ ํธ์ถํ์ฌ jwt token์ ๋ฐ๊ธ๋ฐ์.

login๋ฉ์๋๋ ํ๋ผ๋ฏธํฐ๋ก ๋๊ฒจ๋ฐ์ accessToken์ผ๋ก ํ์ ์ ๋ณด๋ฅผ ์กฐํํ๊ณ  ํ์์ Email๋ก jwt ํ ํฐ์ ์์ฑํจ. ํ ํฐ์ ์์ฑํ๋ ๋ฉ์๋๋ TokenProvider.createTokenDto๋ฉฐ ์กฐํํ ํ์ ์ ๋ณด์ email์ด ๋ฉ์๋์ ํ๋ผ๋ฏธํฐ๋ก ๋ค์ด๊ฐ.

<br>

### ์ฃผ์ ์ฝ๋
***
#### - ๋ก๊ทธ์ธ API

:: login ๋ฉ์๋๋ฅผ ํธ์ถํ์ฌ jwtํ ํฐ์ ๋ฐ๊ธ๋ฐ์
```java
ResponseJwtTokenDto responseJwtTokenDto = loginService.login(accessToken, socialType);
```
<br>

#### - LoginService.login()
:: ํ์ ์ ๋ณด ์กฐํ
```java
//OAuth ๋ก๊ทธ์ธ - ์์ ํ์ ์ ๋ณด ์กฐํ
OAuthAttributes oAuthAttributes = getSocialUserInfo(accessToken, socialType);

// ํผ ๋ก๊ทธ์ธ - ํ์ ์ ๋ณด(์ด๋ฉ์ผ) ์กฐํ
Member member = memberService.findByEmail(formLoginRequestDto.getEmail());
```

:: ํ ํฐ ์์ฑ
```java
TokenDto tokenDto = tokenProvider.createTokenDto(oAuthAttributes.getEmail());
```
<br>

#### TokenProvider.createTokenDTO(email)
:: TokenDto ์์ฑ
```java
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
```
