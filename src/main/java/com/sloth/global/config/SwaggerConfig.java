package com.sloth.global.config;

import com.sloth.domain.member.Member;
import com.sloth.global.resolver.CurrentEmail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket commonApi() {
        Server devServer = new Server("Dev Server", "http://dev.nanagong.net", "for dev server", Collections.emptyList(), Collections.emptyList());
        Server localServer = new Server("Local", "http://localhost:8080", "for local usages", Collections.emptyList(), Collections.emptyList());
        return new Docket(DocumentationType.OAS_30)
                .ignoredParameterTypes(Member.class)
                .ignoredParameterTypes(Errors.class)
                .ignoredParameterTypes(Member.class)
                .ignoredParameterTypes(Errors.class)
                .ignoredParameterTypes(BindingResult.class)
                .ignoredParameterTypes(CurrentEmail.class)
                .servers(devServer, localServer)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .groupName("sloth")
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sloth.api"))
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("sloth api info")
                .description("sloth API")
                .version("1.0.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

}