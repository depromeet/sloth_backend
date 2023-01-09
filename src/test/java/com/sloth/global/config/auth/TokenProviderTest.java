package com.sloth.global.config.auth;

import com.sloth.global.config.auth.dto.TokenDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    @Test
    void test() {
        TokenProvider tokenProvider = new TokenProvider();
        TokenDto tokenDto = tokenProvider.createTokenDto("mraz3068@gmail.com");
        System.out.println(tokenDto);
    }

}