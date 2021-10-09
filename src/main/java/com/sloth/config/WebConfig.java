package com.sloth.config;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import com.sloth.config.auth.TokenProvider;
import com.sloth.config.auth.interceptor.AuthInterceptor;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.memberToken.repository.MemberTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenProvider tokenProvider;
    private final MemberTokenRepository memberTokenRepository;
    private final MemberRepository memberRepository;
    private final MessageSource messageSource;

    @Bean
    public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new XssEscapeServletFilter());
        filterRegistration.setOrder(1);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(tokenProvider, memberTokenRepository);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/health", "/api/profile");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                );
    }

}