### **🖌 OAuth 로그인, 폼 로그인 후 jwt 토큰 발급받기**
<br>

### 해결 방법
***
클라이언트에게 accessToken을 받은 후 올바른 토큰 값이면 LoginService에 있는 login 메서드를 호출하여 jwt token을 발급받음.

login메서드는 파라미터로 넘겨받은 accessToken으로 회원 정보를 조회하고 회원의 Email로 jwt 토큰을 생성함. 토큰을 생성하는 메서드는 TokenProvider.createTokenDto며 조회한 회원 정보의 email이 메서드의 파라미터로 들어감.


### 주요 코드
***
#### - 로그인 API

:: login 메서드를 호출하여 jwt토큰을 발급받음
```java
ResponseJwtTokenDto responseJwtTokenDto = loginService.login(accessToken, socialType);
```
<br>

#### - LoginService.login()
:: 회원 정보 조회
```java
//OAuth 로그인 - 소셜 회원 정보 조회
OAuthAttributes oAuthAttributes = getSocialUserInfo(accessToken, socialType);

// 폼 로그인 - 회원 정보(이메일) 조회
Member member = memberService.findByEmail(formLoginRequestDto.getEmail());
```

:: 토큰 생성
```java
TokenDto tokenDto = tokenProvider.createTokenDto(oAuthAttributes.getEmail());
```
<br>

#### TokenProvider.createTokenDTO(email)
:: TokenDto 생성
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