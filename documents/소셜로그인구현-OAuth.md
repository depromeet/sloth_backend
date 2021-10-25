# 로그인 구현
## 구현하고자 하는 기능
소셜 로그인 요청 시 회원정보 가져와 가입 후 `JWT TOKEN` 생성하여 반환

## OAuth 2.0 절차
![OAuth 2.0 GIF](https://res.cloudinary.com/practicaldev/image/fetch/s--AHu-wF84--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_66%2Cw_880/https://dev-to-uploads.s3.amazonaws.com/i/2j7kqc7qabtfpl250jf2.gif)  
[GIF Source](https://dev.to/hem/oauth-2-0-flows-explained-in-gifs-2o7a)
1. SNS에 등록한 서비스의 정보 (`client_id`,`client_secret`,`scope`,`state`,`redirect_uri`)를 포함해 `authorization URL`로 요청
2. `Authorization Server`에서 `Resource owner`에게 `permission`여부 묻기
3. `permission`확인되면 `Authorization Server`에서 `client`의 `redirect_uri`로 `authorization code` 반환
4. `client`에서 `Authorization Server`로 서비스 정보와 함께 `authorization code` 요청
5. `Authorization Server`에서 검사 후 `client`로 `Access Token` 반환
6. `client`에서 `Resource Server`로 `Access Token`과 함께 API 요청
7. `Resource Server`에서 검사 후 `client`로 API 결과 반환
## 방식
```java
/**
* OAuth 로그인
* @param accessToken
* @param socialType
* @return
*/
public ResponseJwtTokenDto login(String accessToken, SocialType socialType) {
    // 소셜 회원 정보 조회
    OAuthAttributes oAuthAttributes = getSocialUserInfo(accessToken, socialType);
    // JWT 토큰 생성
    TokenDto tokenDto = tokenProvider.createTokenDto(oAuthAttributes.getEmail());
    // 회원가입
    memberService.saveMember(oAuthAttributes, tokenDto);
    return modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
}
private OAuthAttributes getSocialUserInfo(String accessToken, SocialType socialType) {
    //SNS에 따라 분기 처리
    SocialApiSerivce socialApiSerivce = SocialApiServiceFactory.getSocialApiService(socialType);
    //유저 정보 가져오기
    OAuthAttributes oAuthAttributes = socialApiSerivce.getUserInfo(accessToken);
    return oAuthAttributes;
}
```
   
### 소셜 회원 정보 조회
1. FE에서 Resource Server에서 발급받은 Access Token, SNS 종류와 함께 로그인 request  
[Code 보기](https://github.com/depromeet/sloth_backend/blob/master/src/main/java/com/sloth/api/login/controller/LoginController.java#L48)
2. `SocialApiServiceFactory`에서 SNS 종류에 따라 분기 처리 -> 해당하는 `SocialApiService` 가져오기  
`interface`로 추상화 후 구현, 구현체들이 `DI`시에 `Map`형식으로 주입되어 `Bean`이름으로 분기처리  
[getSocialApiService 구현](https://github.com/depromeet/sloth_backend/blob/718326443413e66bf412aef8f41c9cb3533cae50/src/main/java/com/sloth/api/login/service/SocialApiServiceFactory.java#L17)
3. 가져온 `ApiService` 구현체의 `getUserInfo`메소드에서 `Feign`이용하여 `Resource server`로 회원 정보 가져오는 API request  
[Google API Feign request](https://github.com/depromeet/sloth_backend/blob/718326443413e66bf412aef8f41c9cb3533cae50/src/main/java/com/sloth/api/login/google/GoogleApiServiceImpl.java#L26), [KAKAO API Feign request](https://github.com/depromeet/sloth_backend/blob/718326443413e66bf412aef8f41c9cb3533cae50/src/main/java/com/sloth/api/login/kakao/KakaoApiServiceImpl.java#L26)


### JWT 토큰 생성
### 회원가입
[Code 보기](https://github.com/depromeet/sloth_backend/blob/718326443413e66bf412aef8f41c9cb3533cae50/src/main/java/com/sloth/app/member/service/MemberService.java#L29)
```java
/**
* OAuth 로그인
* @param oAuthAttributes 회원정보
* @param tokenDto JWT토큰
* @return
*/
public void saveMember(OAuthAttributes oAuthAttributes, TokenDto tokenDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuthAttributes.getEmail());

        if(optionalMember.isEmpty()) {
            Member member = Member.createOauthMember(oAuthAttributes);
            Member savedMember = memberRepository.save(member);

            //리프레시 토큰 저장
            saveRefreshToken(savedMember, tokenDto);
        }
    }
```
1. 회원 정보 중 email로 사전 가입 확인
2. [`createOauthMember`메소드](https://github.com/depromeet/sloth_backend/blob/718326443413e66bf412aef8f41c9cb3533cae50/src/main/java/com/sloth/domain/member/Member.java#L86)에서 `Member`생성 후 `repository`에 저장
3. [`saveRefreshToken`메소드](https://github.com/depromeet/sloth_backend/blob/718326443413e66bf412aef8f41c9cb3533cae50/src/main/java/com/sloth/app/member/service/MemberService.java#L55)에서 `refreshToken`을 `MemberToken`이라는 엔티티로 `Member`와 연관관계 매핑하여 저장
4. `ModelMapper`이용해 `JWT`토큰을 `ResponseJwtTokenDto`형태로 감싼 후 반환
```java
return modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
```

## 이슈
- JWT `refresh token` 갱신
- JWT Token으로 request 시 회원 email 가져오기 -> Getmapping 등에서 memberId를 보내기 어렵고 대부분의 요청에 사용자의 memberId 필요한데 그럴때마다 email을 보낼 수 없다. -> `@CurrentMember`