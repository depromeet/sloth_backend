package com.sloth.config;

import com.sloth.domain.member.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket commonApi() {
        Server serverLocal = new Server("local", "http://localhost:8080", "for local usages", Collections.emptyList(), Collections.emptyList());
        Server testServer = new Server("test", "https://slothbackend.hopto.org", "for testing", Collections.emptyList(), Collections.emptyList());
        return new Docket(DocumentationType.OAS_30)
                .ignoredParameterTypes(Member.class)
                .ignoredParameterTypes(Errors.class)
                .ignoredParameterTypes(Member.class)
                .ignoredParameterTypes(Errors.class)
                .ignoredParameterTypes(BindingResult.class)
                .servers(serverLocal, testServer)
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

}