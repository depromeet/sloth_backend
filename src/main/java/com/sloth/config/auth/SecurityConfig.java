package com.sloth.config.auth;

import com.sloth.app.member.service.CustomOAuth2UserService;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.memberToken.repository.MemberTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity  //Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable().headers().frameOptions().disable() //h2-console 화면을 사용하기 위해서 해당 옵션 disable
            .and()
                .authorizeRequests()    //url별 권한 관리를 설정하는 옵션의 시작점
                .antMatchers("/", "/css/**", "**.html", "/images/**", "/js/**"
                        , "/assets/**","/h2-console/**", "/profile", "/swagger-ui.html").permitAll()
                .antMatchers("/members/login").permitAll()
                //.anyRequest().authenticated()
            .and()
                .oauth2Login().loginPage("/members/login")
            .and()
                .logout().logoutSuccessUrl("/")
            .and()
                .oauth2Login()  //oauth2 로그인 기능에 대한 여러 설정의 진입점
                    .successHandler(new LoginSuccessHandler(memberRepository, memberTokenRepository, tokenProvider))
                    .userInfoEndpoint() //Oauth2 로그인 성공 후 사용자 정보를 가져올 때의 설정 담당
                    .userService(customOAuth2UserService)   //소셜 로그인 성공 후 후속 조치를 조치할 UserService 인터페이스 구현체 등록
                                                            //리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자하는 기능 명시 가능

        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}